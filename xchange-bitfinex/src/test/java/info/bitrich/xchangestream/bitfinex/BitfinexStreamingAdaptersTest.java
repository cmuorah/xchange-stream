package info.bitrich.xchangestream.bitfinex;

import info.bitrich.xchangestream.bitfinex.dto.BitfinexWebSocketAuthOrder;
import info.bitrich.xchangestream.bitfinex.dto.BitfinexWebSocketAuthTrade;
import org.junit.Test;
import org.knowm.xchange.currency.CurrencyPair;
import org.knowm.xchange.dto.Order;
import org.knowm.xchange.dto.Order.OrderStatus;
import org.knowm.xchange.dto.Order.OrderType;
import org.knowm.xchange.dto.trade.LimitOrder;
import org.knowm.xchange.dto.trade.UserTrade;

import java.util.Collections;
import java.util.Date;

import static org.junit.Assert.assertEquals;

public class BitfinexStreamingAdaptersTest {

    @Test
    public void testMarketOrder() {
        BitfinexWebSocketAuthOrder bitfinexWebSocketAuthOrder = new BitfinexWebSocketAuthOrder(
                123123123L, //id,
                0L, //groupId,
                456456456L, //cid,
                "tBTCUSD", //symbol,
                1548674205259L, //mtsCreateamount
                1548674205259L, //mtsUpdate
                new Double("0.000"), //amount
                new Double("0.004"), //amountOrig
                "MARKET", // type
                null, //typePrev
                "EXECUTED @ 3495.1(0.004)", //orderStatus
                new Double("3495.2"), //price
                new Double("3495.2"), //priceAvg
                0d, //priceTrailing
                0d, //priceAuxLimit
                0, //placedId
                0 //flags
        );

        // TODO awaiting https://github.com/knowm/XChange/pull/2907 then I can add market order
        // support to XChange itself. In the meantime these are returned as limit orders.

        Order adaptedOrder = BitfinexStreamingAdapters.adaptOrder(bitfinexWebSocketAuthOrder);

        assertEquals("123123123", adaptedOrder.getId());
        assertEquals(Order.OrderType.BID, adaptedOrder.getType());
        assertEquals(new Double("3495.2"), adaptedOrder.getAveragePrice());
        assertEquals(new Double("0.004"), adaptedOrder.getCumulativeAmount());
        assertEquals(CurrencyPair.BTC_USD, adaptedOrder.getCurrencyPair());

        // TODO see above. should be:
        //assertEquals(Collections.singleton(BitfinexOrderFlags.MARGIN), adaptedOrder.getOrderFlags());

        assertEquals(new Double("0.004"), adaptedOrder.getOriginalAmount());
        assertEquals(new Double("0.000"), adaptedOrder.getRemainingAmount());
        assertEquals(OrderStatus.FILLED, adaptedOrder.getStatus());
        assertEquals(1548674205259L, adaptedOrder.getTimestamp().longValue());
    }

    @Test
    public void testStopOrder() {
        BitfinexWebSocketAuthOrder bitfinexWebSocketAuthOrder = new BitfinexWebSocketAuthOrder(
                123123123L, //id,
                0L, //groupId,
                456456456L, //cid,
                "tBTCUSD", //symbol,
                1548674205259L, //mtsCreateamount
                1548674205259L, //mtsUpdate
                new Double("0.000"), //amount
                new Double("0.004"), //amountOrig
                "STOP", // type
                null, //typePrev
                "EXECUTED @ 3495.1(0.004)", //orderStatus
                new Double("3495.2"), //price
                new Double("3495.2"), //priceAvg
                0d, //priceTrailing
                0d, //priceAuxLimit
                0, //placedId
                0 //flags
        );

        // TODO awaiting https://github.com/knowm/XChange/pull/2907 then I can add market order
        // support to XChange itself. In the meantime these are returned as limit orders.

        Order adaptedOrder = BitfinexStreamingAdapters.adaptOrder(bitfinexWebSocketAuthOrder);

        assertEquals("123123123", adaptedOrder.getId());
        assertEquals(Order.OrderType.BID, adaptedOrder.getType());
        assertEquals(new Double("3495.2"), adaptedOrder.getAveragePrice());
        assertEquals(new Double("0.004"), adaptedOrder.getCumulativeAmount());
        assertEquals(CurrencyPair.BTC_USD, adaptedOrder.getCurrencyPair());

        // TODO see above. should be:
        //assertEquals(Collections.singleton(BitfinexOrderFlags.MARGIN), adaptedOrder.getOrderFlags());

        assertEquals(new Double("0.004"), adaptedOrder.getOriginalAmount());
        assertEquals(new Double("0.000"), adaptedOrder.getRemainingAmount());
        assertEquals(OrderStatus.FILLED, adaptedOrder.getStatus());
        assertEquals(1548674205259L, adaptedOrder.getTimestamp().longValue());
    }

    @Test
    public void testNewLimitOrder() {
        BitfinexWebSocketAuthOrder bitfinexWebSocketAuthOrder = new BitfinexWebSocketAuthOrder(
                123123123L, //id,
                0L, //groupId,
                456456456L, //cid,
                "tBTCUSD", //symbol,
                1548674205259L, //mtsCreateamount
                1548674205267L, //mtsUpdate
                new Double("0.004"), //amount
                new Double("0.004"), //amountOrig
                "EXCHANGE LIMIT", // type
                null, //typePrev
                "ACTIVE", //orderStatus
                new Double("3495.2"), //price
                0d, //priceAvg
                0d, //priceTrailing
                0d, //priceAuxLimit
                0, //placedId
                0 //flags
        );

        LimitOrder adaptedOrder = (LimitOrder) BitfinexStreamingAdapters.adaptOrder(bitfinexWebSocketAuthOrder);

        assertEquals("123123123", adaptedOrder.getId());
        assertEquals(Order.OrderType.BID, adaptedOrder.getType());
        assertEquals(0d, adaptedOrder.getAveragePrice(), 1e-8);
        assertEquals(0.0, adaptedOrder.getCumulativeAmount(), 1e-8);
        assertEquals(CurrencyPair.BTC_USD, adaptedOrder.getCurrencyPair());
        assertEquals(new Double("3495.2"), adaptedOrder.getLimitPrice());
        assertEquals(Collections.emptySet(), adaptedOrder.getOrderFlags());
        assertEquals(new Double("0.004"), adaptedOrder.getOriginalAmount());
        assertEquals(new Double("0.004"), adaptedOrder.getRemainingAmount());
        assertEquals(OrderStatus.NEW, adaptedOrder.getStatus());
        assertEquals(1548674205259L, adaptedOrder.getTimestamp().longValue());
    }


    @Test
    public void testCancelledLimitOrder() {
        BitfinexWebSocketAuthOrder bitfinexWebSocketAuthOrder = new BitfinexWebSocketAuthOrder(
                123123123L, //id,
                0L, //groupId,
                456456456L, //cid,
                "tBTCUSD", //symbol,
                1548674205259L, //mtsCreateamount
                1548674205267L, //mtsUpdate
                new Double("-0.004"), //amount
                new Double("-0.004"), //amountOrig
                "LIMIT", // type
                null, //typePrev
                "CANCELED", //orderStatus
                new Double("3495.2"), //price
                0d, //priceAvg
                0d, //priceTrailing
                0d, //priceAuxLimit
                0, //placedId
                0 //flags
        );

        LimitOrder adaptedOrder = (LimitOrder) BitfinexStreamingAdapters.adaptOrder(bitfinexWebSocketAuthOrder);

        assertEquals("123123123", adaptedOrder.getId());
        assertEquals(Order.OrderType.ASK, adaptedOrder.getType());
        assertEquals(0d, adaptedOrder.getAveragePrice(), 1e-8);
        assertEquals(0.0, adaptedOrder.getCumulativeAmount(), 1e-8);
        assertEquals(CurrencyPair.BTC_USD, adaptedOrder.getCurrencyPair());
        assertEquals(new Double("3495.2"), adaptedOrder.getLimitPrice());
        assertEquals(new Double("0.004"), adaptedOrder.getOriginalAmount());
        assertEquals(new Double("0.004"), adaptedOrder.getRemainingAmount());

        // TODO see above. should be:
        //assertEquals(Collections.singleton(BitfinexOrderFlags.MARGIN), adaptedOrder.getOrderFlags());

        assertEquals(OrderStatus.CANCELED, adaptedOrder.getStatus());
        assertEquals(1548674205259L, adaptedOrder.getTimestamp().longValue());
    }

    @Test
    public void testPartiallyFilledLimitOrder() {
        BitfinexWebSocketAuthOrder bitfinexWebSocketAuthOrder = new BitfinexWebSocketAuthOrder(
                123123123L, //id,
                0L, //groupId,
                456456456L, //cid,
                "tBTCUSD", //symbol,
                1548674205259L, //mtsCreateamount
                1548674205267L, //mtsUpdate
                new Double("-0.001"), //amount
                new Double("-0.004"), //amountOrig
                "LIMIT", // type
                null, //typePrev
                "PARTIALLY FILLED @ 3495.1(0.003)", //orderStatus
                new Double("3495.2"), //price
                new Double("3495.1"), //priceAvg
                0d, //priceTrailing
                0d, //priceAuxLimit
                0, //placedId
                0 //flags
        );

        LimitOrder adaptedOrder = (LimitOrder) BitfinexStreamingAdapters.adaptOrder(bitfinexWebSocketAuthOrder);

        assertEquals("123123123", adaptedOrder.getId());
        assertEquals(Order.OrderType.ASK, adaptedOrder.getType());
        assertEquals(new Double("3495.1"), adaptedOrder.getAveragePrice());
        assertEquals(new Double("0.003"), adaptedOrder.getCumulativeAmount());
        assertEquals(CurrencyPair.BTC_USD, adaptedOrder.getCurrencyPair());
        assertEquals(new Double("3495.2"), adaptedOrder.getLimitPrice());
        assertEquals(new Double("0.004"), adaptedOrder.getOriginalAmount());
        assertEquals(new Double("0.001"), adaptedOrder.getRemainingAmount());

        // TODO see above. should be:
        //assertEquals(Collections.singleton(BitfinexOrderFlags.MARGIN), adaptedOrder.getOrderFlags());

        assertEquals(OrderStatus.PARTIALLY_FILLED, adaptedOrder.getStatus());
        assertEquals(1548674205259L, adaptedOrder.getTimestamp().longValue());
    }


    @Test
    public void testExecutedLimitOrder() {
        BitfinexWebSocketAuthOrder bitfinexWebSocketAuthOrder = new BitfinexWebSocketAuthOrder(
                123123123L, //id,
                0L, //groupId,
                456456456L, //cid,
                "tBTCUSD", //symbol,
                1548674205259L, //mtsCreateamount
                1548674205267L, //mtsUpdate
                0d, //amount
                new Double("0.004"), //amountOrig
                "EXCHANGE LIMIT", // type
                null, //typePrev
                "EXECUTED @ 3495.1(0.004): was PARTIALLY FILLED @ 3495.1(0.003)", //orderStatus
                new Double("3495.2"), //price
                new Double("3495.1"), //priceAvg
                0d, //priceTrailing
                0d, //priceAuxLimit
                0, //placedId
                0 //flags
        );

        LimitOrder adaptedOrder = (LimitOrder) BitfinexStreamingAdapters.adaptOrder(bitfinexWebSocketAuthOrder);

        assertEquals("123123123", adaptedOrder.getId());
        assertEquals(Order.OrderType.BID, adaptedOrder.getType());
        assertEquals(new Double("3495.1"), adaptedOrder.getAveragePrice());
        assertEquals(new Double("0.004"), adaptedOrder.getCumulativeAmount());
        assertEquals(CurrencyPair.BTC_USD, adaptedOrder.getCurrencyPair());
        assertEquals(new Double("3495.2"), adaptedOrder.getLimitPrice());
        assertEquals(new Double("0.004"), adaptedOrder.getOriginalAmount());
        assertEquals(new Double("0.000"), adaptedOrder.getRemainingAmount());
        assertEquals(Collections.emptySet(), adaptedOrder.getOrderFlags());
        assertEquals(OrderStatus.FILLED, adaptedOrder.getStatus());
        assertEquals(1548674205259L, adaptedOrder.getTimestamp().longValue());
    }

    @Test
    public void testTradeBuy() {
        BitfinexWebSocketAuthTrade bitfinexWebSocketAuthTrade = new BitfinexWebSocketAuthTrade(
                335015622L, //id
                "tBTCUSD", //pair
                1548674247684L, //mtsCreate
                21895093123L, //orderId
                new Double("0.00341448"), //execAmount
                new Double("3495.4"), //execPrice
                "SHOULDNT MATTER", //orderType
                new Double("3495.9"), //orderPrice
                1548674247683L, //maker
                new Double("-0.00000682896"), //fee
                "BTC" //feeCurrency
        );
        UserTrade adapted = BitfinexStreamingAdapters.adaptUserTrade(bitfinexWebSocketAuthTrade);
        assertEquals(CurrencyPair.BTC_USD, adapted.getCurrencyPair());
        assertEquals(new Double("0.00000682896"), adapted.getFeeAmount());
        assertEquals(CurrencyPair.BTC_USD.base, adapted.getFeeCurrency());
        assertEquals("335015622", adapted.getId());
        assertEquals("21895093123", adapted.getOrderId());
        assertEquals(new Double("0.00341448"), adapted.getOriginalAmount());
        assertEquals(new Double("3495.4"), adapted.getPrice());
        assertEquals(1548674247684L, adapted.getTimestamp().longValue());
        assertEquals(OrderType.BID, adapted.getType());
    }


    @Test
    public void testTradeSell() {
        BitfinexWebSocketAuthTrade bitfinexWebSocketAuthTrade = new BitfinexWebSocketAuthTrade(
                335015622L, //id
                "tBTCUSD", //pair
                1548674247684L, //mtsCreate
                21895093123L, //orderId
                new Double("-0.00341448"), //execAmount
                new Double("3495.4"), //execPrice
                "SHOULDNT MATTER", //orderType
                new Double("3495.9"), //orderPrice
                1548674247683L, //maker
                new Double("0.00000682896"), //fee
                "BTC" //feeCurrency
        );
        UserTrade adapted = BitfinexStreamingAdapters.adaptUserTrade(bitfinexWebSocketAuthTrade);
        assertEquals(CurrencyPair.BTC_USD, adapted.getCurrencyPair());
        assertEquals(new Double("0.00000682896"), adapted.getFeeAmount());
        assertEquals(CurrencyPair.BTC_USD.base, adapted.getFeeCurrency());
        assertEquals("335015622", adapted.getId());
        assertEquals("21895093123", adapted.getOrderId());
        assertEquals(new Double("0.00341448"), adapted.getOriginalAmount());
        assertEquals(new Double("3495.4"), adapted.getPrice());
        assertEquals(1548674247684L, adapted.getTimestamp().longValue());
        assertEquals(OrderType.ASK, adapted.getType());
    }
}