package info.bitrich.xchangestream.cexio;

import info.bitrich.xchangestream.cexio.dto.CexioWebSocketOrder;
import info.bitrich.xchangestream.cexio.dto.CexioWebSocketOrderBookSubscribeResponse;
import info.bitrich.xchangestream.cexio.dto.CexioWebSocketPair;
import org.knowm.xchange.currency.CurrencyPair;
import org.knowm.xchange.dto.Order;
import org.knowm.xchange.dto.Order.OrderType;
import org.knowm.xchange.dto.marketdata.OrderBook;
import org.knowm.xchange.dto.trade.LimitOrder;

import java.util.Date;
import java.util.List;

public class CexioAdapters {

    private static final int PRECISION = 3;
    private static final Double SATOSHI_SCALE = 100000000d;

    static Order adaptOrder(CexioWebSocketOrder order) {
        if (order.getType() != null) {
            return new CexioOrder(
                    adaptOrderType(order.getType()),
                    adaptCurrencyPair(order.getPair()),
                    adaptAmount(order.getRemains()),
                    order.getId(),
                    order.getTime(),
                    order.getPrice(),
                    order.getFee(),
                    getOrderStatus(order));
        } else {
            return new CexioOrder(
                    adaptCurrencyPair(order.getPair()),
                    order.getId(),
                    getOrderStatus(order),
                    adaptAmount(order.getRemains()));
        }
    }

    private static CurrencyPair adaptCurrencyPair(CexioWebSocketPair pair) {
        return new CurrencyPair(pair.getSymbol1(), pair.getSymbol2());
    }

    private static Order.OrderType adaptOrderType(String type) {
        if (type == null) {
            return null;
        }

        switch (type) {
            case "buy":
                return Order.OrderType.BID;
            case "sell":
                return Order.OrderType.ASK;
            default:
                return null;
        }
    }

    private static Double adaptAmount(Double amount) {
        if (amount == null) {
            return null;
        }

        return amount / SATOSHI_SCALE;
    }

    private static Order.OrderStatus getOrderStatus(CexioWebSocketOrder order) {
        Order.OrderStatus status;
        if (order.isCancel()) {
            status = Order.OrderStatus.CANCELED;
        } else if (order.getRemains().compareTo(0d) == 0) {
            status = Order.OrderStatus.FILLED;
        } else if (order.getType() != null) {
            status = Order.OrderStatus.NEW;
        } else {
            status = Order.OrderStatus.PARTIALLY_FILLED;
        }
        return status;
    }

    private static OrderBook updateOrInsertQuantityForPrice(
            OrderBook orderBook,
            List<List<Double>> updatedQuantitiesPerPrice,
            OrderType orderType,
            Date timestamp,
            CurrencyPair currencyPair,
            String id) {
        for (List<Double> updatedQuantityPrice : updatedQuantitiesPerPrice) {
            if (updatedQuantityPrice.size() != 2) {
                throw new IllegalArgumentException(
                        "Expected price quantity list to contain exactly two big decimals");
            }
            Double price = updatedQuantityPrice.get(0);
            Double quantity = updatedQuantityPrice.get(1);
            LimitOrder orderForUpdatedQuantityPrice = new LimitOrder(orderType, quantity, currencyPair, id, timestamp.getTime(), price);
            orderBook.update(orderForUpdatedQuantityPrice);
        }
        return orderBook;
    }

    // Copied from xchange org.knowm.xchange.cexio.CexIOAdapters since that implementation
    // is private as of the time of writing
    protected static CurrencyPair adaptCurrencyPair(String pair) {
        // Currency pair is in the format: "BCH:USD"
        return new CurrencyPair(pair.replace(":", "/"));
    }

    protected static OrderBook adaptOrderBookIncremental(
            OrderBook prevOrderBook, CexioWebSocketOrderBookSubscribeResponse update) {
        CurrencyPair currencyPair = adaptCurrencyPair(update.pair);
        prevOrderBook =
                updateOrInsertQuantityForPrice(
                        prevOrderBook,
                        update.asks,
                        OrderType.ASK,
                        update.timestamp,
                        currencyPair,
                        update.id.toString());
        prevOrderBook =
                updateOrInsertQuantityForPrice(
                        prevOrderBook,
                        update.bids,
                        OrderType.BID,
                        update.timestamp,
                        currencyPair,
                        update.id.toString());
        return prevOrderBook;
    }
}
