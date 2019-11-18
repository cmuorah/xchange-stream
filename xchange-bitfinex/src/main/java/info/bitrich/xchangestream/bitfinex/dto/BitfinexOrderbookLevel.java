package info.bitrich.xchangestream.bitfinex.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.knowm.xchange.bitfinex.v1.dto.marketdata.BitfinexLevel;


/**
 * Created by Lukas Zaoralek on 8.11.17.
 */
@JsonFormat(shape = JsonFormat.Shape.ARRAY)
@JsonPropertyOrder({"price","count","amount"})
public class BitfinexOrderbookLevel {

    public Double price;

    public Double count;

    public Double amount;

    public BitfinexOrderbookLevel() {
    }

    public BitfinexOrderbookLevel(Double price, Double count, Double amount) {
        this.price = price;
        this.amount = amount;
        this.count = count;
    }

    public Double getPrice() {
        return price;
    }

    public Double getAmount() {
        return amount;
    }

    public Double getCount() {
        return count;
    }

    public BitfinexLevel toBitfinexLevel() {
        // Xchange-bitfinex adapter expects the timestamp to be seconds since Epoch.
        return new BitfinexLevel(price, amount, (double) (System.currentTimeMillis() / 1000));
    }
}
