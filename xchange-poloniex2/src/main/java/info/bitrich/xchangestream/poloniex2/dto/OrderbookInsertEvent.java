package info.bitrich.xchangestream.poloniex2.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;

import java.util.Iterator;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * Created by Lukas Zaoralek on 11.11.17.
 */
public class OrderbookInsertEvent {
    public static final int ASK_SIDE = 0;
    public static final int BID_SIDE = 1;

    private final String currencyPair;
    private final JsonNode[] orderbookSides;


    public OrderbookInsertEvent(@JsonProperty("currencyPair") String currencyPair,
                                @JsonProperty("orderBook") JsonNode[] orderbookSides) {
        this.currencyPair = currencyPair;
        this.orderbookSides = orderbookSides;
    }

    public String getCurrencyPair() {
        return currencyPair;
    }

    public JsonNode[] getOrderbookSides() {
        return orderbookSides;
    }

    public SortedMap<Double, Double> toDepthLevels(int side) {
        if (side == ASK_SIDE) return toDepthLevels(orderbookSides[ASK_SIDE], false);
        else return toDepthLevels(orderbookSides[BID_SIDE], true);
    }

    private SortedMap<Double, Double> toDepthLevels(JsonNode side, boolean reverse) {
        SortedMap<Double, Double> levels = new TreeMap<>(reverse ? java.util.Collections.reverseOrder() : null);
        Iterator<String> prices = side.fieldNames();
        while (prices.hasNext()) {
            String strPrice = prices.next();
            Double price = new Double(strPrice);
            Double volume = new Double(side.get(strPrice).asText());
            levels.put(price, volume);
        }

        return levels;
    }
}
