package info.bitrich.xchangestream.okcoin;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.reactivex.Observable;
import io.reactivex.observers.TestObserver;
import org.junit.Before;
import org.junit.Test;
import org.knowm.xchange.currency.CurrencyPair;
import org.knowm.xchange.dto.Order;
import org.knowm.xchange.dto.marketdata.OrderBook;
import org.knowm.xchange.dto.trade.LimitOrder;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class OkCoinStreamingMarketDataServiceTest {

    @Mock
    private OkCoinStreamingService okCoinStreamingService;
    private OkCoinStreamingMarketDataService marketDataService;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        marketDataService = new OkCoinStreamingMarketDataService(okCoinStreamingService);
    }

    @Test
    public void testGetOrderBook() throws Exception {
        // Given order book in JSON
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(ClassLoader.getSystemClassLoader().getResourceAsStream("order-book.json"));

        when(okCoinStreamingService.subscribeChannel(any())).thenReturn(Observable.just(jsonNode));

        long timestamp = 1484602135246L;

        List<LimitOrder> bids = new ArrayList<>();
        bids.add(new LimitOrder(Order.OrderType.BID, Double.parseDouble("0.922"), CurrencyPair.BTC_USD, null, timestamp, Double.parseDouble("819.9")));
        bids.add(new LimitOrder(Order.OrderType.BID, Double.parseDouble("0.085"), CurrencyPair.BTC_USD, null, timestamp, Double.parseDouble("818.63")));

        List<LimitOrder> asks = new ArrayList<>();
        asks.add(new LimitOrder(Order.OrderType.ASK, Double.parseDouble("0.035"), CurrencyPair.BTC_USD, null, timestamp, Double.parseDouble("821.6")));
        asks.add(new LimitOrder(Order.OrderType.ASK, Double.parseDouble("5.18"), CurrencyPair.BTC_USD, null, timestamp, Double.parseDouble("821.65")));
        asks.add(new LimitOrder(Order.OrderType.ASK, Double.parseDouble("2.89"), CurrencyPair.BTC_USD, null, timestamp, Double.parseDouble("821.7")));

        OrderBook expected = new OrderBook(timestamp, asks, bids);

        // Call get order book observable
        TestObserver<OrderBook> test = marketDataService.getOrderBook(CurrencyPair.BTC_USD).test();

        // Get order book object in correct order
        test.assertResult(expected);
    }
}
