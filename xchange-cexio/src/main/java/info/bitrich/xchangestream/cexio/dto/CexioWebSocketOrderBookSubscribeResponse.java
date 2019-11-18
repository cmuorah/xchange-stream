package info.bitrich.xchangestream.cexio.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigInteger;
import java.util.Date;
import java.util.List;

public class CexioWebSocketOrderBookSubscribeResponse {
    @JsonProperty
    public final Date timestamp;

    @JsonProperty
    public final List<List<Double>> bids;

    @JsonProperty
    public final List<List<Double>> asks;

    @JsonProperty
    public final String pair;

    @JsonProperty
    public final BigInteger id;

    public CexioWebSocketOrderBookSubscribeResponse(
            @JsonProperty(value = "timestamp") Date timestamp,
            @JsonProperty(value = "time") Date time,
            @JsonProperty("bids") List<List<Double>> bids,
            @JsonProperty("asks") List<List<Double>> asks,
            @JsonProperty("pair") String pair,
            @JsonProperty("id") BigInteger id) {
        if (timestamp == null && time == null) {
            throw new IllegalArgumentException("Both time and timestamp cannot be null");
        }
        this.timestamp = timestamp != null ? timestamp : time;
        this.bids = bids;
        this.asks = asks;
        this.pair = pair;
        this.id = id;
    }
}
