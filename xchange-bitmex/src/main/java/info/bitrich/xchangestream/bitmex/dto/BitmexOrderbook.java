package info.bitrich.xchangestream.bitmex.dto;

import org.knowm.xchange.dto.marketdata.OrderBook;
import org.knowm.xchange.dto.trade.LimitOrder;

import java.util.*;

import static info.bitrich.xchangestream.bitmex.dto.BitmexLimitOrder.ASK_SIDE;
import static info.bitrich.xchangestream.bitmex.dto.BitmexLimitOrder.BID_SIDE;

/**
 * Created by Lukas Zaoralek on 13.11.17.
 */
public class BitmexOrderbook {
    private SortedMap<Double, BitmexLimitOrder> asks;
    private SortedMap<Double, BitmexLimitOrder> bids;

    private Map<String, Double> askIds;
    private Map<String, Double> bidIds;

    public BitmexOrderbook() {
        this.askIds = new HashMap<>();
        this.bidIds = new HashMap<>();
        this.asks = new TreeMap<>();
        this.bids = new TreeMap<>(java.util.Collections.reverseOrder());
    }

    public BitmexOrderbook(BitmexLimitOrder[] levels) {
        this();
        createFromLevels(levels);
    }

    public static List<LimitOrder> toLimitOrders(BitmexLimitOrder[] levels) {
        if (levels == null || levels.length == 0) return null;

        List<LimitOrder> limitOrders = new ArrayList<>(levels.length);
        for (BitmexLimitOrder level : levels) {
            LimitOrder limitOrder = level.toLimitOrder();
            limitOrders.add(limitOrder);
        }

        return limitOrders;
    }

    public void createFromLevels(BitmexLimitOrder[] levels) {
        for (BitmexLimitOrder level : levels) {
            SortedMap<Double, BitmexLimitOrder> orderBookSide = level.getSide().equals(ASK_SIDE) ? asks : bids;
            Map<String, Double> orderBookSideIds = level.getSide().equals(ASK_SIDE) ? askIds : bidIds;
            orderBookSide.put(level.getPrice(), level);
            orderBookSideIds.put(level.getId(), level.getPrice());
        }
    }

    public void updateLevels(BitmexLimitOrder[] levels, String action) {
        for (BitmexLimitOrder level : levels) {
            updateLevel(level, action);
        }
    }

    public void updateLevel(BitmexLimitOrder level, String action) {
        SortedMap<Double, BitmexLimitOrder> orderBookSide = level.getSide().equals(ASK_SIDE) ? asks : bids;
        Map<String, Double> orderBookSideIds = level.getSide().equals(ASK_SIDE) ? askIds : bidIds;

        if (action.equals("insert")) {
            orderBookSide.put(level.getPrice(), level);
            orderBookSideIds.put(level.getId(), level.getPrice());
        } else if (action.equals("delete") || action.equals("update")) {
            boolean shouldDelete = action.equals("delete");
            String id = level.getId();
            Double price = orderBookSideIds.get(id);
            orderBookSide.remove(price);
            orderBookSideIds.remove(id);
            if (!shouldDelete) {
                BitmexLimitOrder modifiedLevel = new BitmexLimitOrder(level.getSymbol(), level.getId(), level.getSide(), price,
                        level.getSize()); // Original level doesn't have price! see bitmex doc
                orderBookSide.put(price, modifiedLevel);
                orderBookSideIds.put(id, price);
            }
        }
    }

    public BitmexLimitOrder[] getLevels(String side) {
        SortedMap<Double, BitmexLimitOrder> orderBookSide = side.equals(ASK_SIDE) ? asks : bids;
        return orderBookSide.values().toArray(new BitmexLimitOrder[orderBookSide.size()]);
    }

    public BitmexLimitOrder[] getAsks() {
        return getLevels(ASK_SIDE);
    }

    public BitmexLimitOrder[] getBids() {
        return getLevels(BID_SIDE);
    }

    public OrderBook toOrderbook() {
        List<LimitOrder> orderbookAsks = toLimitOrders(getAsks());
        List<LimitOrder> orderbookBids = toLimitOrders(getBids());
        return new OrderBook(null, orderbookAsks, orderbookBids);
    }
}
