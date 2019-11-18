package info.bitrich.xchangestream.bitmex.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.knowm.xchange.dto.marketdata.Ticker;

/**
 * Created by Lukas Zaoralek on 13.11.17.
 */
public class BitmexTicker extends BitmexMarketDataEvent {
    private final String timestamp;
    private final String symbol;
    private final Double bidSize;
    private final Double bidPrice;
    private final Double askPrice;
    private final Double askSize;

    public BitmexTicker(@JsonProperty("timestamp") String timestamp,
                        @JsonProperty("symbol") String symbol,
                        @JsonProperty("bidSize") Double bidSize,
                        @JsonProperty("bidPrice") Double bidPrice,
                        @JsonProperty("askPrice") Double askPrice,
                        @JsonProperty("askSize") Double askSize) {
        super(symbol, timestamp);
        this.timestamp = timestamp;
        this.symbol = symbol;
        this.bidSize = bidSize;
        this.bidPrice = bidPrice;
        this.askPrice = askPrice;
        this.askSize = askSize;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public String getSymbol() {
        return symbol;
    }

    public Double getBidSize() {
        return bidSize;
    }

    public Double getBidPrice() {
        return bidPrice;
    }

    public Double getAskPrice() {
        return askPrice;
    }

    public Double getAskSize() {
        return askSize;
    }

    public Ticker toTicker() {
        return new Ticker.Builder()
                .ask(askPrice).bidSize(bidSize)
                .bid(bidPrice).askSize(askSize)
                .timestamp(getDate()).currencyPair(getCurrencyPair())
                .build();
    }
}
