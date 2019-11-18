package info.bitrich.xchangestream.binance.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.knowm.xchange.currency.Currency;
import org.knowm.xchange.dto.account.Balance;

public final class BinanceWebsocketBalance {

    private final Currency currency;
    private final Double free;
    private final Double locked;

    public BinanceWebsocketBalance(@JsonProperty("a") String asset, @JsonProperty("f") Double free, @JsonProperty("l") Double locked) {
        this.currency = Currency.getInstance(asset);
        this.locked = locked;
        this.free = free;
    }

    public Currency getCurrency() {
        return currency;
    }

    public Double getTotal() {
        return free + locked;
    }

    public Double getAvailable() {
        return free;
    }

    public Double getLocked() {
        return locked;
    }

    public Balance toBalance() {
        return new Balance(currency, getTotal(), getAvailable(), getLocked());
    }

    @Override
    public String toString() {
        return "Balance[currency=" + currency + ", free=" + free + ", locked=" + locked + "]";
    }
}