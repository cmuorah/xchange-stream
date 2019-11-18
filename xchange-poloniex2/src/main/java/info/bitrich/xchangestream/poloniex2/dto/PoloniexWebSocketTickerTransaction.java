package info.bitrich.xchangestream.poloniex2.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.knowm.xchange.currency.CurrencyPair;
import org.knowm.xchange.poloniex.dto.marketdata.PoloniexMarketData;
import org.knowm.xchange.poloniex.dto.marketdata.PoloniexTicker;


/**
 * Created by Lukas Zaoralek on 11.11.17.
 */
@JsonFormat(shape = JsonFormat.Shape.ARRAY)
public class PoloniexWebSocketTickerTransaction {
    public String channelId;
    public String timestamp;
    public String[] ticker;

    public PoloniexTicker toPoloniexTicker(CurrencyPair currencyPair) {
        PoloniexMarketData poloniexMarketData = new PoloniexMarketData();
        Double last = Double.parseDouble(ticker[1]);
        Double lowestAsk = Double.parseDouble(ticker[2]);
        Double highestBid = Double.parseDouble(ticker[3]);
        Double percentChange = Double.parseDouble(ticker[4]);
        Double baseVolume = Double.parseDouble(ticker[5]);
        Double quoteVolume = Double.parseDouble(ticker[6]);
        Double isFrozen = Double.parseDouble(ticker[7]);
        Double high24hr = Double.parseDouble(ticker[8]);
        Double low24hr = Double.parseDouble(ticker[9]);
        poloniexMarketData.setLast(last);
        poloniexMarketData.setLowestAsk(lowestAsk);
        poloniexMarketData.setHighestBid(highestBid);
        poloniexMarketData.setPercentChange(percentChange);
        poloniexMarketData.setBaseVolume(baseVolume);
        poloniexMarketData.setQuoteVolume(quoteVolume);
        poloniexMarketData.setHigh24hr(high24hr);
        poloniexMarketData.setLow24hr(low24hr);
        return new PoloniexTicker(poloniexMarketData, currencyPair);
    }

    public int getPairId() {
        return new Integer(ticker[0]);
    }
}
