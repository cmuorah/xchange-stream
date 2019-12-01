package info.bitrich.xchangestream.bitflyer.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.knowm.xchange.currency.Currency;
import org.knowm.xchange.currency.CurrencyPair;
import org.knowm.xchange.dto.marketdata.Ticker;


/**
 * Created by Lukas Zaoralek on 15.11.17.
 */
public class BitflyerTicker extends BitflyerMarketEvent {
    private final String productCode;
    private final String tickId;
    private final Double bestBid;
    private final Double bestAsk;
    private final Double bestBidSize;
    private final Double bestAskSize;
    private final Double totalBidDepth;
    private final Double totalAskDepth;
    private final String ltp;
    private final Double volume;
    private final Double volumeByProduct;

    public BitflyerTicker(@JsonProperty("product_code") String productCode,
                          @JsonProperty("timestamp") String timestamp,
                          @JsonProperty("tick_id") String tickId,
                          @JsonProperty("best_bid") Double bestBid,
                          @JsonProperty("best_ask") Double bestAsk,
                          @JsonProperty("best_bid_size") Double bestBidSize,
                          @JsonProperty("best_ask_size") Double bestAskSize,
                          @JsonProperty("total_bid_depth") Double totalBidDepth,
                          @JsonProperty("total_ask_depth") Double totalAskDepth,
                          @JsonProperty("ltp") String ltp,
                          @JsonProperty("volume") Double volume,
                          @JsonProperty("volume_by_product") Double volumeByProduct) {
        super(timestamp);
        this.productCode = productCode;
        this.tickId = tickId;
        this.bestBid = bestBid;
        this.bestAsk = bestAsk;
        this.bestBidSize = bestBidSize;
        this.bestAskSize = bestAskSize;
        this.totalBidDepth = totalBidDepth;
        this.totalAskDepth = totalAskDepth;
        this.ltp = ltp;
        this.volume = volume;
        this.volumeByProduct = volumeByProduct;
    }

    public String getProductCode() {
        return productCode;
    }

    public String getTickId() {
        return tickId;
    }

    public Double getBestBid() {
        return bestBid;
    }

    public Double getBestAsk() {
        return bestAsk;
    }

    public Double getBestBidSize() {
        return bestBidSize;
    }

    public Double getBestAskSize() {
        return bestAskSize;
    }

    public Double getTotalBidDepth() {
        return totalBidDepth;
    }

    public Double getTotalAskDepth() {
        return totalAskDepth;
    }

    public String getLtp() {
        return ltp;
    }

    public Double getVolume() {
        return volume;
    }

    public Double getVolumeByProduct() {
        return volumeByProduct;
    }

    public CurrencyPair getCurrencyPair() {
        String[] currencies = productCode.split("_");
        String base = currencies[0];
        String counter = currencies[1];
        return new CurrencyPair(new Currency(base), new Currency(counter));
    }

    public Ticker toTicker() {
        return new Ticker.Builder().ask(bestAsk).bid(bestBid).volume(volume)
                .timestamp(getDate().getTime()).currencyPair(getCurrencyPair()).build();
    }
}
