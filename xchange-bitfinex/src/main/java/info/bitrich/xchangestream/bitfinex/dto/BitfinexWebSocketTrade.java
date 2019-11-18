package info.bitrich.xchangestream.bitfinex.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.knowm.xchange.bitfinex.v1.dto.marketdata.BitfinexTrade;


/**
 * Created by Lukas Zaoralek on 7.11.17.
 */
@JsonFormat(shape = JsonFormat.Shape.ARRAY)
public class BitfinexWebSocketTrade {
    public long tradeId;
    public long timestamp;
    public Double amount;
    public Double price;

    public BitfinexWebSocketTrade() {
    }

    public BitfinexWebSocketTrade(long tradeId, long timestamp, Double amount, Double price) {
        this.tradeId = tradeId;
        this.timestamp = timestamp;
        this.amount = amount;
        this.price = price;
    }

    public long getTradeId() {
        return tradeId;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public Double getAmount() {
        return amount;
    }

    public Double getPrice() {
        return price;
    }

    public BitfinexTrade toBitfinexTrade() {
        String type;
        if (amount.compareTo(0d) < 0) {
            type = "sell";
        } else {
            type = "buy";
        }
        return new BitfinexTrade(price, Math.abs(amount), timestamp / 1000, "bitfinex", tradeId, type);
    }
}
