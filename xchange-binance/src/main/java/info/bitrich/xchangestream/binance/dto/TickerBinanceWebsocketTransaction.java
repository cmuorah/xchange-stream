package info.bitrich.xchangestream.binance.dto;

import com.fasterxml.jackson.annotation.JsonProperty;


import org.knowm.xchange.binance.dto.marketdata.BinanceTicker24h;

public class TickerBinanceWebsocketTransaction extends ProductBinanceWebSocketTransaction {

    private final BinanceTicker24h ticker;

    public TickerBinanceWebsocketTransaction(
            @JsonProperty("e") String eventType,
            @JsonProperty("E") String eventTime,
            @JsonProperty("s") String symbol,
            @JsonProperty("p") Double priceChange,
            @JsonProperty("P") Double priceChangePercent,
            @JsonProperty("w") Double weightedAvgPrice,
            @JsonProperty("x") Double prevClosePrice,
            @JsonProperty("c") Double lastPrice,
            @JsonProperty("Q") Double lastQty,
            @JsonProperty("b") Double bidPrice,
            @JsonProperty("B") Double bidQty,
            @JsonProperty("a") Double askPrice,
            @JsonProperty("A") Double askQty,
            @JsonProperty("o") Double openPrice,
            @JsonProperty("h") Double highPrice,
            @JsonProperty("l") Double lowPrice,
            @JsonProperty("v") Double volume,
            @JsonProperty("q") Double quoteVolume,
            @JsonProperty("O") Long openTime,
            @JsonProperty("C") Long closeTime,
            @JsonProperty("F") Long firstId,
            @JsonProperty("L") Long lastId,
            @JsonProperty("n") Long count) {

        super(eventType, eventTime, symbol);

        ticker = new BinanceTicker24h(
                priceChange,
                priceChangePercent,
                weightedAvgPrice,
                prevClosePrice,
                lastPrice,
                lastQty,
                bidPrice,
                bidQty,
                askPrice,
                askQty,
                openPrice,
                highPrice,
                lowPrice,
                volume,
                quoteVolume,
                openTime,
                closeTime,
                firstId,
                lastId,
                count,
                symbol);
        ticker.setCurrencyPair(currencyPair);
    }

    public BinanceTicker24h getTicker() {
        return ticker;
    }

}
