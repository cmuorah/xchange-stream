package info.bitrich.xchange.coinmate.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.knowm.xchange.coinmate.dto.marketdata.CoinmateTransactionsEntry;


public class CoinmateWebSocketTrade {
    private final long timestamp;
    private final Double price;
    private final Double amount;

    public CoinmateWebSocketTrade(@JsonProperty("date") long timestamp, @JsonProperty("price") Double price, @JsonProperty("amount") Double amount) {
        this.timestamp = timestamp;
        this.price = price;
        this.amount = amount;
    }

    public CoinmateTransactionsEntry toTransactionEntry(String currencyPair) {
        return new CoinmateTransactionsEntry(timestamp, null, price, amount, currencyPair);
    }
}
