package info.bitrich.xchangestream.poloniex2.dto;

import org.knowm.xchange.poloniex.dto.marketdata.PoloniexDepth;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;

/**
 * Created by Lukas Zaoralek on 11.11.17.
 */
public class PoloniexOrderbook {
    public static final Double zero = 0d;

    private SortedMap<Double, Double> asks;
    private SortedMap<Double, Double> bids;

    public PoloniexOrderbook(SortedMap<Double, Double> asks, SortedMap<Double, Double> bids) {
        this.asks = asks;
        this.bids = bids;
    }

    public void modify(OrderbookModifiedEvent modifiedEvent) {
        SortedMap<Double, Double> side = modifiedEvent.getType().equals("0") ? asks : bids;
        Double price = modifiedEvent.getPrice();
        Double volume = modifiedEvent.getVolume();

        side.remove(price);
        if (volume.compareTo(zero) != 0) {
            side.put(price, volume);
        }
    }

    private List<List<Double>> toPoloniexDepthLevels(SortedMap<Double, Double> side) {
        List<List<Double>> poloniexDepthSide = new ArrayList<>(side.size());
        for (Map.Entry<Double, Double> level : side.entrySet()) {
            List<Double> poloniexLevel = new ArrayList<>(2);
            poloniexLevel.add(level.getKey());
            poloniexLevel.add(level.getValue());
            poloniexDepthSide.add(poloniexLevel);
        }

        return poloniexDepthSide;
    }

    public PoloniexDepth toPoloniexDepth() {
        PoloniexDepth orderbook = new PoloniexDepth();

        List<List<Double>> poloniexDepthAsk = toPoloniexDepthLevels(asks);
        List<List<Double>> poloniexDepthBid = toPoloniexDepthLevels(bids);

        orderbook.setAsks(poloniexDepthAsk);
        orderbook.setBids(poloniexDepthBid);
        return orderbook;
    }
}
