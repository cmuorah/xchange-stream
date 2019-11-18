package info.bitrich.xchangestream.bitfinex.dto;

import org.knowm.xchange.bitfinex.v1.dto.marketdata.BitfinexDepth;
import org.knowm.xchange.bitfinex.v1.dto.marketdata.BitfinexLevel;

import java.util.*;


/**
 * Created by Lukas Zaoralek on 8.11.17.
 */
public class BitfinexOrderbook {
    private Map<Double, BitfinexOrderbookLevel> asks;
    private Map<Double, BitfinexOrderbookLevel> bids;

    public BitfinexOrderbook(BitfinexOrderbookLevel[] levels) {
        createFromLevels(levels);
    }

    private void createFromLevels(BitfinexOrderbookLevel[] levels) {
        this.asks = new HashMap<>(levels.length / 2);
        this.bids = new HashMap<>(levels.length / 2);

        for (BitfinexOrderbookLevel level : levels) {

            if (level.getCount().compareTo(0d) == 0)
                continue;

            if (level.getAmount().compareTo(0d) > 0)
                bids.put(level.getPrice(), level);
            else
                asks.put(level.getPrice(),
                        new BitfinexOrderbookLevel(
                                level.getPrice(),
                                level.getCount(),
                                Math.abs(level.getAmount())
                        ));
        }
    }

    public synchronized BitfinexDepth toBitfinexDepth() {
        SortedMap<Double, BitfinexOrderbookLevel> bitfinexLevelAsks = new TreeMap<>();
        SortedMap<Double, BitfinexOrderbookLevel> bitfinexLevelBids = new TreeMap<>(java.util.Collections.reverseOrder());

        for (Map.Entry<Double, BitfinexOrderbookLevel> level : asks.entrySet()) {
            bitfinexLevelAsks.put(level.getValue().getPrice(), level.getValue());
        }

        for (Map.Entry<Double, BitfinexOrderbookLevel> level : bids.entrySet()) {
            bitfinexLevelBids.put(level.getValue().getPrice(), level.getValue());
        }

        List<BitfinexLevel> askLevels = new ArrayList<>(asks.size());
        List<BitfinexLevel> bidLevels = new ArrayList<>(bids.size());
        for (Map.Entry<Double, BitfinexOrderbookLevel> level : bitfinexLevelAsks.entrySet()) {
            askLevels.add(level.getValue().toBitfinexLevel());
        }
        for (Map.Entry<Double, BitfinexOrderbookLevel> level : bitfinexLevelBids.entrySet()) {
            bidLevels.add(level.getValue().toBitfinexLevel());
        }

        return new BitfinexDepth(askLevels.toArray(new BitfinexLevel[0]),
                bidLevels.toArray(new BitfinexLevel[0]));
    }

    public synchronized void updateLevel(BitfinexOrderbookLevel level) {


        Map<Double, BitfinexOrderbookLevel> side;

        // Determine side and normalize negative ask amount values
        BitfinexOrderbookLevel bidAskLevel = level;
        if (level.getAmount().compareTo(0d) < 0) {
            side = asks;
            bidAskLevel = new BitfinexOrderbookLevel(
                    level.getPrice(),
                    level.getCount(),
                    Math.abs(level.getAmount())
            );
        } else {
            side = bids;
        }

        boolean shouldDelete = bidAskLevel.getCount().compareTo(0d) == 0;

        side.remove(bidAskLevel.getPrice());
        if (!shouldDelete) {
            side.put(bidAskLevel.getPrice(), bidAskLevel);
        }
    }
}
