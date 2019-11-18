package info.bitrich.xchangestream.bitstamp.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class BitstampOrderBook {
    private final long timestamp;
    private final List<List<Double>> bids;
    private final List<List<Double>> asks;

    public BitstampOrderBook(@JsonProperty("timestamp") long timestamp, @JsonProperty("bids") List<List<Double>> bids, @JsonProperty("asks") List<List<Double>> asks) {
        this.timestamp = timestamp;
        this.bids = bids;
        this.asks = asks;
    }

    public List<List<Double>> getBids() {
        return bids;
    }

    public List<List<Double>> getAsks() {
        return asks;
    }

    public long getTimestamp() {
        return timestamp;
    }
}
