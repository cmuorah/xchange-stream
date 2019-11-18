package info.bitrich.xchangestream.bitfinex.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.knowm.xchange.bitfinex.v1.dto.marketdata.BitfinexTicker;


/**
 * Created by Lukas Zaoralek on 8.11.17.
 */
@JsonFormat(shape = JsonFormat.Shape.ARRAY)
public class BitfinexWebSocketTickerTransaction {

    public String channelId;
    public String[] tickerArr;

    public BitfinexWebSocketTickerTransaction() {
    }

    public BitfinexWebSocketTickerTransaction(String channelId, String[] tickerArr) {
        this.channelId = channelId;
        this.tickerArr = tickerArr;
    }

    public String getChannelId() {
        return channelId;
    }

    public BitfinexTicker toBitfinexTicker() {
        Double bid = new Double(tickerArr[0]);
        Double ask = new Double(tickerArr[2]);
        Double mid = (ask - bid) / 2.0;
        Double low = new Double(tickerArr[9]);
        Double high = new Double(tickerArr[8]);
        Double last = new Double(tickerArr[6]);
        // Xchange-bitfinex adapter expects the timestamp to be seconds since Epoch.
        double timestamp = System.currentTimeMillis() / 1000.0;
        Double volume = new Double(tickerArr[7]);

        return new BitfinexTicker(mid, bid, ask, low, high, last, timestamp, volume);
    }
}
