package info.bitrich.xchangestream.coinbasepro.dto;

import com.jsoniter.annotation.JsonProperty;
import com.jsoniter.fuzzy.MaybeStringDoubleDecoder;
import info.bitrich.xchangestream.coinbasepro.CoinbaseProStreamingAdapters;
import net.openhft.chronicle.wire.AbstractMarshallable;
import org.knowm.xchange.coinbasepro.dto.marketdata.CoinbaseProProductStats;
import org.knowm.xchange.coinbasepro.dto.marketdata.CoinbaseProProductTicker;
import org.knowm.xchange.coinbasepro.dto.marketdata.CoinbaseProTrade;
import org.knowm.xchange.coinbasepro.dto.trade.CoinbaseProFill;
import org.knowm.xchange.currency.CurrencyPair;
import org.knowm.xchange.dto.Order.OrderType;
import org.knowm.xchange.dto.marketdata.OrderBook;
import org.knowm.xchange.dto.trade.LimitOrder;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Domain object mapping a CoinbasePro web socket message.
 */
public class CoinbaseProWebSocketTransaction extends AbstractMarshallable {
    private String type;
    @JsonProperty("order_id")
    private String orderId;
    @JsonProperty("order_type")
    private String orderType;
    @JsonProperty(decoder = MaybeStringDoubleDecoder.class)
    private Double size;
    @JsonProperty(value = "remaining_size", decoder = MaybeStringDoubleDecoder.class)
    private Double remainingSize;
    @JsonProperty(decoder = MaybeStringDoubleDecoder.class)
    private Double price;
    @JsonProperty(value = "best_bid", decoder = MaybeStringDoubleDecoder.class)
    private Double bestBid;
    @JsonProperty(value = "best_ask", decoder = MaybeStringDoubleDecoder.class)
    private Double bestAsk;
    @JsonProperty(value = "last_size", decoder = MaybeStringDoubleDecoder.class)
    private Double lastSize;
    @JsonProperty(value = "volume_24h", decoder = MaybeStringDoubleDecoder.class)
    private Double volume24h;
    @JsonProperty(value = "open_24h", decoder = MaybeStringDoubleDecoder.class)
    private Double open24h;
    @JsonProperty(value = "low_24h", decoder = MaybeStringDoubleDecoder.class)
    private Double low24h;
    @JsonProperty(value = "high_24h", decoder = MaybeStringDoubleDecoder.class)
    private Double high24h;
    private String side;
    private String[][] bids;
    private String[][] asks;
    private String[][] changes;
    @JsonProperty("client_oid")
    private String clientOid;
    @JsonProperty("product_id")
    private String productId;
    private long sequence;
    private String time;
    private String reason;
    @JsonProperty("trade_id")
    private long tradeId;
    @JsonProperty("maker_order_id")
    private String makerOrderId;
    @JsonProperty("taker_order_id")
    private String takerOrderId;
    @JsonProperty("taker_user_id")
    private String takerUserId;
    @JsonProperty("user_id")
    private String userId;
    @JsonProperty("taker_profile_id")
    private String takerProfileId;
    @JsonProperty("profile_id")
    private String profileId;



    public CoinbaseProWebSocketTransaction() {
    }

    private List<LimitOrder> coinbaseProOrderBookChanges(String side, OrderType orderType, CurrencyPair currencyPair, String[][] changes, SortedMap<Double, Double> sideEntries,
                                                         int maxDepth) {
        if (changes.length == 0) {
            return Collections.emptyList();
        }

        if (sideEntries == null) {
            return Collections.emptyList();
        }

        for (String[] level : changes) {
            if (level.length == 3 && !level[0].equals(side)) {
                continue;
            }

            Double price = new Double(level[level.length - 2]);
            Double volume = new Double(level[level.length - 1]);
            sideEntries.put(price, volume);
        }

        Stream<Entry<Double, Double>> stream = sideEntries.entrySet()
                .stream()
                .filter(level -> level.getValue().compareTo(0d) != 0);
        if (maxDepth != 0) {
            stream = stream.limit(maxDepth);
        }
        return stream.map(level -> new LimitOrder(
                orderType,
                level.getValue(),
                currencyPair,
                "0",
                null,
                level.getKey()))
                .collect(Collectors.toList());
    }

    public OrderBook toOrderBook(SortedMap<Double, Double> bids, SortedMap<Double, Double> asks, int maxDepth, CurrencyPair currencyPair) {
        // For efficiency, we go straight to XChange format
        List<LimitOrder> gdaxOrderBookBids = coinbaseProOrderBookChanges("buy", OrderType.BID, currencyPair, changes != null ? changes : this.bids,
                bids, maxDepth);
        List<LimitOrder> gdaxOrderBookAsks = coinbaseProOrderBookChanges("sell", OrderType.ASK, currencyPair, changes != null ? changes : this.asks,
                asks, maxDepth);
        return new OrderBook(time == null ? null : CoinbaseProStreamingAdapters.parseDate(time), gdaxOrderBookAsks, gdaxOrderBookBids, false);
    }

    public CoinbaseProProductTicker toCoinbaseProProductTicker() {
        String tickerTime = time;
        if (tickerTime == null) {
            SimpleDateFormat dateFormatGmt = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
            dateFormatGmt.setTimeZone(TimeZone.getTimeZone("UTC"));
            tickerTime = dateFormatGmt.format(new Date()); //First ticker event doesn't have time!
        }
        return new CoinbaseProProductTicker(String.valueOf(tradeId), price, lastSize, bestBid, bestAsk, volume24h, tickerTime);
    }

    public CoinbaseProProductStats toCoinbaseProProductStats() {
        return new CoinbaseProProductStats(open24h, high24h, low24h, volume24h);
    }

    public CoinbaseProTrade toCoinbaseProTrade() {
        return new CoinbaseProTrade(time, tradeId, price, size, side, makerOrderId, takerOrderId);
    }

    public CoinbaseProFill toCoinbaseProFill() {
        boolean taker = userId != null && userId.equals(takerUserId);
        // buy/sell are flipped on the taker side.
        String useSide = side;
        if (taker && side != null) {
            if ("buy".equals(side)) {
                useSide = "sell";
            } else {
                useSide = "buy";
            }
        }
        return new CoinbaseProFill(String.valueOf(tradeId), productId, price, size, taker ? takerOrderId : makerOrderId, time, null, null, true, useSide);
    }

    public String getType() {
        return type;
    }

    public String getOrderId() {
        return orderId;
    }

    public String getOrderType() {
        return orderType;
    }

    public Double getSize() {
        return size;
    }

    public Double getPrice() {
        return price;
    }

    public Double getBestBid() {
        return bestBid;
    }

    public Double getBestAsk() {
        return bestAsk;
    }

    public Double getLastSize() {
        return lastSize;
    }

    public Double getVolume24h() {
        return volume24h;
    }

    public Double getOpen24h() {
        return open24h;
    }

    public Double getLow24h() {
        return low24h;
    }

    public Double getHigh24h() {
        return high24h;
    }

    public String getSide() {
        return side;
    }

    public String getClientOid() {
        return clientOid;
    }

    public String getProductId() {
        return productId;
    }

    public Long getSequence() {
        return sequence;
    }

    public String getTime() {
        return time;
    }

    public Double getRemainingSize() {
        return remainingSize;
    }

    public String getReason() {
        return reason;
    }

    public long getTradeId() {
        return tradeId;
    }

    public String getMakerOrderId() {
        return makerOrderId;
    }


    public String getTakerOrderId() {
        return takerOrderId;
    }

    public String getTakerUserId() {
        return takerUserId;
    }

    public String getUserId() {
        return userId;
    }

    public String getTakerProfileId() {
        return takerProfileId;
    }

    public String getProfileId() {
        return profileId;
    }

}
