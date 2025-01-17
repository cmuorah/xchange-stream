package info.bitrich.xchange.coinmate;

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
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

public class CoinmateStreamingMarketDataServiceTest {
    @Mock
    private PusherStreamingService streamingService;
    private CoinmateStreamingMarketDataService marketDataService;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        marketDataService = new CoinmateStreamingMarketDataService(streamingService);
    }

    @Test
    public void testGetOrderBook() throws Exception {
        // Given order book in JSON
        String orderBook = new String(Files.readAllBytes(Paths.get(ClassLoader.getSystemResource("order-book.json").toURI())));

        when(streamingService.subscribeChannel(eq("order_book-BTC_EUR"), eq("order_book"))).thenReturn(Observable.just(orderBook));

        List<LimitOrder> bids = new ArrayList<>();
        bids.add(new LimitOrder(Order.OrderType.BID, new Double("2.48345723"), CurrencyPair.BTC_EUR, null, null, new Double("852.8")));
        bids.add(new LimitOrder(Order.OrderType.BID, new Double("0.50521505"), CurrencyPair.BTC_EUR, null, null, new Double("850.37")));

        List<LimitOrder> asks = new ArrayList<>();
        asks.add(new LimitOrder(Order.OrderType.ASK, new Double("0.04"), CurrencyPair.BTC_EUR, null, null, new Double("853.35")));
        asks.add(new LimitOrder(Order.OrderType.ASK, new Double("11.89247706"), CurrencyPair.BTC_EUR, null, null, new Double("854.5")));
        asks.add(new LimitOrder(Order.OrderType.ASK, new Double("0.38478732"), CurrencyPair.BTC_EUR, null, null, new Double("855.48")));

        // Call get order book observable
        TestObserver<OrderBook> test = marketDataService.getOrderBook(CurrencyPair.BTC_EUR).test();

        // We get order book object in correct order
        test.assertValue(orderBook1 -> {
            assertThat(orderBook1.getAsks()).as("Asks").isEqualTo(asks);
            assertThat(orderBook1.getBids()).as("Bids").isEqualTo(bids);
            return true;
        });

        test.assertNoErrors();
    }

    @Test
    public void testGetTrades() throws Exception {
        String trade = new String(Files.readAllBytes(Paths.get(ClassLoader.getSystemResource("trades.json").toURI())));

        when(streamingService.subscribeChannel(eq("trades-BTC_CZK"), eq("new_trades"))).thenReturn(Observable.just(trade));

        Trade expected1 = new Trade(null, new Double("0.08233888"), CurrencyPair.BTC_CZK, new Double("855.29"), 1484863030522L, null);
        Trade expected2 = new Trade(null, new Double("0.00200428"), CurrencyPair.BTC_CZK, new Double("855.13"), 1484863028887L, null);

        TestObserver<Trade> test = marketDataService.getTrades(CurrencyPair.BTC_CZK).test();

        test.assertValueAt(0, trade1 -> {
            assertThat(trade1.getId()).as("Id").isEqualTo(expected1.getId());
            assertThat(trade1.getCurrencyPair()).as("Currency pair").isEqualTo(expected1.getCurrencyPair());
            assertThat(trade1.getPrice()).as("Price").isEqualTo(expected1.getPrice());
            assertThat(trade1.getTimestamp()).as("Timestamp").isEqualTo(expected1.getTimestamp());
            assertThat(trade1.getOriginalAmount()).as("Amount").isEqualTo(expected1.getOriginalAmount());
            assertThat(trade1.getType()).as("Type").isEqualTo(expected1.getType());
            return true;
        });

        test.assertValueAt(1, trade1 -> {
            assertThat(trade1.getId()).as("Id").isEqualTo(expected2.getId());
            assertThat(trade1.getCurrencyPair()).as("Currency pair").isEqualTo(expected2.getCurrencyPair());
            assertThat(trade1.getPrice()).as("Price").isEqualTo(expected2.getPrice());
            assertThat(trade1.getTimestamp()).as("Timestamp").isEqualTo(expected2.getTimestamp());
            assertThat(trade1.getOriginalAmount()).as("Amount").isEqualTo(expected2.getOriginalAmount());
            assertThat(trade1.getType()).as("Type").isEqualTo(expected2.getType());
            return true;
        });

        test.assertNoErrors();
    }

    @Test(expected = NotAvailableFromExchangeException.class)
    public void testGetTicker() throws Exception {
        marketDataService.getTicker(CurrencyPair.BTC_EUR).test();
    }
}