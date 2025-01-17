package info.bitrich.xchangestream.bitstamp;

import info.bitrich.xchangestream.service.pusher.PusherStreamingService;
import io.reactivex.Observable;
import io.reactivex.observers.TestObserver;
import org.junit.Before;
import org.junit.Test;
import org.knowm.xchange.currency.CurrencyPair;
import org.knowm.xchange.dto.Order;
import org.knowm.xchange.dto.marketdata.OrderBook;
import org.knowm.xchange.dto.marketdata.Trade;
import org.knowm.xchange.dto.trade.LimitOrder;
import org.knowm.xchange.exceptions.NotAvailableFromExchangeException;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.function.Supplier;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@SuppressWarnings("ResultOfMethodCallIgnored")
public class BitstampStreamingMarketDataServiceTest extends BitstampStreamingMarketDataServiceBaseTest {
    @Mock
    private PusherStreamingService streamingService;
    private BitstampStreamingMarketDataService marketDataService;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        marketDataService = new BitstampStreamingMarketDataService(streamingService);
    }

    private void testOrderbookCommon(String channelName, Supplier<TestObserver<OrderBook>> updater) throws Exception {
        // Given order book in JSON
        String orderBook = new String(Files.readAllBytes(Paths.get(ClassLoader.getSystemResource("order-book.json").toURI())));

        when(streamingService.subscribeChannel(eq(channelName), eq("data"))).thenReturn(Observable.just(orderBook));

        List<LimitOrder> bids = new ArrayList<>();
        bids.add(new LimitOrder(Order.OrderType.BID, new Double("0.922"), CurrencyPair.BTC_EUR, "", null, new Double("819.9")));
        bids.add(new LimitOrder(Order.OrderType.BID, new Double("0.085"), CurrencyPair.BTC_EUR, "", null, new Double("818.63")));

        List<LimitOrder> asks = new ArrayList<>();
        asks.add(new LimitOrder(Order.OrderType.ASK, new Double("2.89"), CurrencyPair.BTC_EUR, "", null, new Double("821.7")));
        asks.add(new LimitOrder(Order.OrderType.ASK, new Double("5.18"), CurrencyPair.BTC_EUR, "", null, new Double("821.65")));
        asks.add(new LimitOrder(Order.OrderType.ASK, new Double("0.035"), CurrencyPair.BTC_EUR, "", null, new Double("821.6")));

        // Call get order book observable
        TestObserver<OrderBook> test = updater.get();

        // We get order book object in correct order
        validateOrderBook(bids, asks, test);
    }

    @Test
    public void testGetDifferentialOrderBook() throws Exception {
        testOrderbookCommon("diff_order_book_btceur", () -> marketDataService.getDifferentialOrderBook(CurrencyPair.BTC_EUR).test());
    }

    @Test
    public void testGetOrderBook() throws Exception {
        testOrderbookCommon("order_book_btceur", () -> marketDataService.getOrderBook(CurrencyPair.BTC_EUR).test());
    }

    @Test
    public void testGetTrades() throws Exception {
        // Given order book in JSON
        String trade = new String(Files.readAllBytes(Paths.get(ClassLoader.getSystemResource("trade.json").toURI())));

        when(streamingService.subscribeChannel(eq("live_trades"), eq("trade"))).thenReturn(Observable.just(trade));

        Trade expected = new Trade(Order.OrderType.ASK, new Double("34.390000000000001"), CurrencyPair.BTC_USD, new Double("914.38999999999999"), 1484858423000L, "177827396");

        // Call get order book observable
        TestObserver<Trade> test = marketDataService.getTrades(CurrencyPair.BTC_USD).test();

        // We get order book object in correct order
        validateTrades(expected, test);
    }

    @Test(expected = NotAvailableFromExchangeException.class)
    public void testGetTicker() {
        marketDataService.getTicker(CurrencyPair.BTC_EUR).test();
    }
}