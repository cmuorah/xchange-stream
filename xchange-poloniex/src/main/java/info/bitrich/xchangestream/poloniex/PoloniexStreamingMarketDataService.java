package info.bitrich.xchangestream.poloniex;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.collect.MinMaxPriorityQueue;
import info.bitrich.xchangestream.core.StreamingMarketDataService;
import info.bitrich.xchangestream.poloniex.utils.MinMaxPriorityQueueUtils;
import info.bitrich.xchangestream.service.wamp.WampStreamingService;
import io.reactivex.Observable;
import org.knowm.xchange.currency.CurrencyPair;
import org.knowm.xchange.dto.Order;
import org.knowm.xchange.dto.marketdata.OrderBook;
import org.knowm.xchange.dto.marketdata.Ticker;
import org.knowm.xchange.dto.marketdata.Trade;
import org.knowm.xchange.dto.trade.LimitOrder;
import org.knowm.xchange.poloniex.PoloniexAdapters;
import org.knowm.xchange.poloniex.PoloniexUtils;
import org.knowm.xchange.poloniex.dto.marketdata.PoloniexMarketData;
import org.knowm.xchange.poloniex.dto.marketdata.PoloniexPublicTrade;
import org.knowm.xchange.poloniex.dto.marketdata.PoloniexTicker;

import java.util.*;

public class PoloniexStreamingMarketDataService implements StreamingMarketDataService {
    private static final int ORDER_BOOK_LEVELS = 100;
    private final WampStreamingService streamingService;
    Comparator<LimitOrder> asendingPriceComparator = Comparator.comparing(LimitOrder::getLimitPrice);
    Comparator<LimitOrder> descendingPriceComparator = (LimitOrder o1, LimitOrder o2) -> -o1.getLimitPrice().compareTo(o2.getLimitPrice());
    private Map<CurrencyPair, MinMaxPriorityQueue<LimitOrder>> orderBookBids = new HashMap<>();
    private Map<CurrencyPair, MinMaxPriorityQueue<LimitOrder>> orderBookAsks = new HashMap<>();
    public PoloniexStreamingMarketDataService(WampStreamingService streamingService) {
        this.streamingService = streamingService;
    }

    @Override
    public Observable<OrderBook> getOrderBook(CurrencyPair currencyPair, Object... args) {
        MinMaxPriorityQueue<LimitOrder> bidQueue, askQueue;

        if (!orderBookBids.containsKey(currencyPair)) {
            bidQueue = MinMaxPriorityQueue.orderedBy(descendingPriceComparator).expectedSize(ORDER_BOOK_LEVELS).maximumSize(ORDER_BOOK_LEVELS).create();
            orderBookBids.put(currencyPair, bidQueue);
        } else {
            bidQueue = orderBookAsks.get(currencyPair);
        }

        if (!orderBookAsks.containsKey(currencyPair)) {
            askQueue = MinMaxPriorityQueue.orderedBy(asendingPriceComparator).expectedSize(ORDER_BOOK_LEVELS).maximumSize(ORDER_BOOK_LEVELS).create();
            orderBookAsks.put(currencyPair, askQueue);
        } else {
            askQueue = orderBookAsks.get(currencyPair);
        }

        String channel = PoloniexUtils.toPairString(currencyPair);
        return streamingService.subscribeChannel(channel)
                .map(pubSubData -> {
                    long now = System.currentTimeMillis();
                    for (int i = 0; i < pubSubData.arguments().size(); i++) {
                        JsonNode item = pubSubData.arguments().get(i);
                        String type = item.get("type").asText();
                        if ("orderBookRemove".equals(type) || "orderBookModify".equals(type)) {

                            JsonNode data = item.get("data");
                            Double rate = Double.parseDouble(data.get("rate").asText());
                            Double amount = data.has("amount") ? Double.parseDouble(data.get("amount").asText()) : null;
                            String bookType = data.get("type").asText();
                            if ("orderBookRemove".equals(type)) {
                                if ("ask".equals(bookType)) {
                                    askQueue.removeIf(x -> rate.equals(x.getLimitPrice()));
                                } else if ("bid".equals(bookType)) {
                                    bidQueue.removeIf(x -> rate.equals(x.getLimitPrice()));
                                }

                            } else {
                                if ("ask".equals(bookType)) {
                                    LimitOrder level = new LimitOrder(Order.OrderType.ASK, amount, currencyPair, null, now, rate);
                                    askQueue.add(level);
                                } else if ("bid".equals(bookType)) {
                                    LimitOrder level = new LimitOrder(Order.OrderType.BID, amount, currencyPair, null, now, rate);
                                    bidQueue.add(level);
                                }
                            }
                        }
                    }
                    return new OrderBook(now, MinMaxPriorityQueueUtils.toList(askQueue, asendingPriceComparator), MinMaxPriorityQueueUtils.toList(bidQueue, descendingPriceComparator));
                });
    }

    @Override
    public Observable<Ticker> getTicker(CurrencyPair currencyPair, Object... args) {
        return streamingService.subscribeChannel("ticker")
                .map(pubSubData -> {
                    PoloniexMarketData marketData = new PoloniexMarketData();
                    marketData.setLast(Double.parseDouble(pubSubData.arguments().get(1).asText()));
                    marketData.setLowestAsk(Double.parseDouble(pubSubData.arguments().get(2).asText()));
                    marketData.setHighestBid(Double.parseDouble(pubSubData.arguments().get(3).asText()));
                    marketData.setPercentChange(Double.parseDouble(pubSubData.arguments().get(4).asText()));
                    marketData.setBaseVolume(Double.parseDouble(pubSubData.arguments().get(5).asText()));
                    marketData.setQuoteVolume(Double.parseDouble(pubSubData.arguments().get(6).asText()));
                    marketData.setHigh24hr(Double.parseDouble(pubSubData.arguments().get(8).asText()));
                    marketData.setLow24hr(Double.parseDouble(pubSubData.arguments().get(9).asText()));

                    PoloniexTicker ticker = new PoloniexTicker(marketData, PoloniexUtils.toCurrencyPair(pubSubData.arguments().get(0).asText()));
                    return PoloniexAdapters.adaptPoloniexTicker(ticker, ticker.getCurrencyPair());
                })
                .filter(ticker -> ticker.getCurrencyPair().equals(currencyPair));
    }

    @Override
    public Observable<Trade> getTrades(CurrencyPair currencyPair, Object... args) {
        String channel = PoloniexUtils.toPairString(currencyPair);
        return streamingService.subscribeChannel(channel)
                .flatMap(pubSubData -> {
                    List<Trade> res = new ArrayList<Trade>();
                    for (int i = 0; i < pubSubData.arguments().size(); i++) {
                        JsonNode item = pubSubData.arguments().get(i);
                        if ("newTrade".equals(item.get("type").asText())) {
                            JsonNode data = item.get("data");
                            PoloniexPublicTrade trade = new PoloniexPublicTrade();
                            trade.setTradeID(data.get("tradeID").asText());
                            trade.setAmount(Double.parseDouble(data.get("amount").asText("0")));
                            trade.setDate(data.get("date").asText());
                            trade.setRate(Double.parseDouble(data.get("rate").asText("0")));
                            trade.setTotal(Double.parseDouble(data.get("total").asText("0")));
                            trade.setType(data.get("type").asText());

                            res.add(PoloniexAdapters.adaptPoloniexPublicTrade(trade, currencyPair));
                        }

                    }
                    return Observable.fromIterable(res);
                });
    }
}
