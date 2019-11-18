package info.bitrich.xchangestream.okcoin.dto;

import org.knowm.xchange.dto.Order;
import org.knowm.xchange.okcoin.dto.marketdata.OkCoinDepth;

import java.util.Collection;
import java.util.Date;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * Created by Lukas Zaoralek on 16.11.17.
 */
public class OkCoinOrderbook {

    private final SortedMap<Double, Double[]> asks;
    private final SortedMap<Double, Double[]> bids;

    public OkCoinOrderbook() {
        asks = new TreeMap<>(java.util.Collections.reverseOrder()); //Because okcoin adapter uses reverse sort for asks!!!
        bids = new TreeMap<>();
    }

    public OkCoinOrderbook(OkCoinDepth depth) {
        this();
        createFromDepth(depth);
    }

    public void createFromDepth(OkCoinDepth depth) {
        Double[][] depthAsks = depth.getAsks();
        Double[][] depthBids = depth.getBids();

        createFromDepthLevels(depthAsks, Order.OrderType.ASK);
        createFromDepthLevels(depthBids, Order.OrderType.BID);
    }

    public void createFromDepthLevels(Double[][] depthLevels, Order.OrderType side) {
        SortedMap<Double, Double[]> orderbookLevels = side == Order.OrderType.ASK ? asks : bids;
        for (Double[] level : depthLevels) {
            orderbookLevels.put(level[0], level);
        }
    }

    public void updateLevels(Double[][] depthLevels, Order.OrderType side) {
        for (Double[] level : depthLevels) {
            updateLevel(level, side);
        }
    }

    public void updateLevel(Double[] level, Order.OrderType side) {
        SortedMap<Double, Double[]> orderBookSide = side == Order.OrderType.ASK ? asks : bids;
        double zero = 0d;
        boolean shouldDelete = level[1].compareTo(zero) == 0;
        Double price = level[0];
        orderBookSide.remove(price);
        if (!shouldDelete) {
            orderBookSide.put(price, level);
        }
    }

    public Double[][] getSide(Order.OrderType side) {
        SortedMap<Double, Double[]> orderbookLevels = side == Order.OrderType.ASK ? asks : bids;
        Collection<Double[]> levels = orderbookLevels.values();
        return levels.toArray(new Double[orderbookLevels.size()][]);
    }

    public Double[][] getAsks() {
        return getSide(Order.OrderType.ASK);
    }

    public Double[][] getBids() {
        return getSide(Order.OrderType.BID);
    }

    public OkCoinDepth toOkCoinDepth(long epoch) {
        Date timestamp = new java.util.Date(epoch);
        return new OkCoinDepth(getAsks(), getBids(), timestamp);
    }
}
