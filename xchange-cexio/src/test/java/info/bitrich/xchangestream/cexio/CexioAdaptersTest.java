package info.bitrich.xchangestream.cexio;

import info.bitrich.xchangestream.cexio.dto.CexioWebSocketOrderBookSubscribeResponse;
import org.junit.Test;
import org.knowm.xchange.currency.CurrencyPair;
import org.knowm.xchange.dto.Order.OrderType;
import org.knowm.xchange.dto.marketdata.OrderBook;
import org.knowm.xchange.dto.trade.LimitOrder;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class CexioAdaptersTest {

    @Test
    public void testAdaptOrderBookIncremental() {
        OrderBook orderBookSoFar =
                new OrderBook(new Date(), new ArrayList<>(), new ArrayList<>());

        List<List<Double>> askOrders = new ArrayList<>();
        askOrders.add(
                new ArrayList<>(
                        Arrays.asList(10d, 500d)));
        askOrders.add(
                new ArrayList<>(
                        Arrays.asList(11d, 400d)));
        askOrders.add(
                new ArrayList<>(
                        Arrays.asList(13d, 200d)));

        List<List<Double>> bidOrders = new ArrayList<>();
        bidOrders.add(
                new ArrayList<>(
                        Arrays.asList(10d, 200d)));
        bidOrders.add(
                new ArrayList<>(
                        Arrays.asList(9d, 100d)));
        bidOrders.add(
                new ArrayList<>(
                        Arrays.asList(7d, 50d)));

        List<LimitOrder> expectedAsks = new ArrayList<>();
        List<LimitOrder> expectedBids = new ArrayList<>();
        for (int i = 0; i < askOrders.size(); i++) {
            LimitOrder expectedAsk =
                    new LimitOrder(
                            OrderType.ASK,
                            askOrders.get(i).get(1),
                            CurrencyPair.LTC_USD,
                            "1",
                            new Date(1234567),
                            askOrders.get(i).get(0));
            LimitOrder expectedBid =
                    new LimitOrder(
                            OrderType.BID,
                            bidOrders.get(i).get(1),
                            CurrencyPair.LTC_USD,
                            "1",
                            new Date(1234567),
                            bidOrders.get(i).get(0));

            expectedAsks.add(expectedAsk);
            expectedBids.add(expectedBid);
        }

        CexioWebSocketOrderBookSubscribeResponse subResp1 =
                new CexioWebSocketOrderBookSubscribeResponse(
                        new Date(1234567), null, bidOrders, askOrders, "LTC:USD", BigInteger.ONE);
        OrderBook orderBookV1 = CexioAdapters.adaptOrderBookIncremental(orderBookSoFar, subResp1);
        OrderBook expectedOrderBookV1 =
                new OrderBook(new Date(1234567), expectedAsks, expectedBids, true /* sort */);
        assertEquals(expectedOrderBookV1.getBids(), orderBookV1.getBids());
        assertEquals(expectedOrderBookV1.getAsks(), orderBookV1.getAsks());

        List<List<Double>> askOrders2 = new ArrayList<>();
        askOrders2.add(
                new ArrayList<>(
                        Arrays.asList(10d, 400d)));
        askOrders2.add(
                new ArrayList<>(
                        Arrays.asList(11d, 600d)));
        askOrders2.add(
                new ArrayList<>(
                        Arrays.asList(12d, 100d)));
        askOrders2.add(
                new ArrayList<>(
                        Arrays.asList(15d, 50d)));

        List<List<Double>> bidOrders2 = new ArrayList<>();
        bidOrders2.add(
                new ArrayList<>(
                        Arrays.asList(9d, 150d)));
        bidOrders2.add(
                new ArrayList<>(
                        Arrays.asList(8d, 70d)));
        bidOrders2.add(
                new ArrayList<>(
                        Arrays.asList(6d, 30d)));

        CexioWebSocketOrderBookSubscribeResponse subResp2 =
                new CexioWebSocketOrderBookSubscribeResponse(
                        new Date(1235567),
                        null,
                        bidOrders2,
                        askOrders2,
                        "LTC:USD",
                        BigInteger.ONE.add(BigInteger.ONE));

        LimitOrder expectedAsk1 =
                new LimitOrder(
                        OrderType.ASK,
                        400d,
                        CurrencyPair.LTC_USD,
                        "2",
                        new Date(1235567),
                        10d);
        LimitOrder expectedAsk2 =
                new LimitOrder(
                        OrderType.ASK,
                        600d,
                        CurrencyPair.LTC_USD,
                        "2",
                        new Date(1235567),
                        11d);
        LimitOrder expectedAsk3 =
                new LimitOrder(
                        OrderType.ASK,
                        100d,
                        CurrencyPair.LTC_USD,
                        "2",
                        new Date(1235567),
                        12d);
        LimitOrder expectedAsk4 =
                new LimitOrder(
                        OrderType.ASK,
                        50d,
                        CurrencyPair.LTC_USD,
                        "2",
                        new Date(1235567),
                        15d);
        LimitOrder expectedAsk5 =
                new LimitOrder(
                        OrderType.ASK,
                        200d,
                        CurrencyPair.LTC_USD,
                        "1",
                        new Date(1234567),
                        13d);

        LimitOrder expectedBid1 =
                new LimitOrder(
                        OrderType.BID,
                        150d,
                        CurrencyPair.LTC_USD,
                        "2",
                        new Date(1235567),
                        9d);
        LimitOrder expectedBid2 =
                new LimitOrder(
                        OrderType.BID,
                        70d,
                        CurrencyPair.LTC_USD,
                        "2",
                        new Date(1235567),
                        8d);
        LimitOrder expectedBid3 =
                new LimitOrder(
                        OrderType.BID,
                        30d,
                        CurrencyPair.LTC_USD,
                        "2",
                        new Date(1235567),
                        6d);
        LimitOrder expectedBid4 =
                new LimitOrder(
                        OrderType.BID,
                        200d,
                        CurrencyPair.LTC_USD,
                        "1",
                        new Date(1234567),
                        10d);
        LimitOrder expectedBid5 =
                new LimitOrder(
                        OrderType.BID,
                        50d,
                        CurrencyPair.LTC_USD,
                        "1",
                        new Date(1234567),
                        7d);

        List<LimitOrder> expectedAsks2 =
                new ArrayList<>(
                        Arrays.asList(
                                expectedAsk1, expectedAsk2, expectedAsk3, expectedAsk4, expectedAsk5));
        List<LimitOrder> expectedBids2 =
                new ArrayList<>(
                        Arrays.asList(
                                expectedBid1, expectedBid2, expectedBid3, expectedBid4, expectedBid5));

        OrderBook orderBookV2 = CexioAdapters.adaptOrderBookIncremental(orderBookV1, subResp2);
        OrderBook expectedOrderBookV2 =
                new OrderBook(new Date(1235567), expectedAsks2, expectedBids2, true /* sort */);
        assertEquals(expectedOrderBookV2.getBids(), orderBookV2.getBids());
        assertEquals(expectedOrderBookV2.getAsks(), orderBookV2.getAsks());
    }
}
