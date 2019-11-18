package info.bitrich.xchangestream.cexio.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;

public class CexioWebSocketOrder {

    private final String id;
    private final Double remains;
    private final Double fremains;
    private final boolean cancel;
    private final CexioWebSocketPair pair;
    private final Double price;
    private final Double amount;
    private final Date time;
    private final String type;
    private final Double fee;

    public CexioWebSocketOrder(
            @JsonProperty("id") String id,
            @JsonProperty("remains") Double remains,
            @JsonProperty("fremains") Double fremains,
            @JsonProperty("cancel") boolean cancel,
            @JsonProperty("pair") CexioWebSocketPair pair,
            @JsonProperty("price") Double price,
            @JsonProperty("amount") Double amount,
            @JsonProperty("time") Date time,
            @JsonProperty("type") String type,
            @JsonProperty("fee") Double fee) {
        this.id = id;
        this.remains = remains;
        this.fremains = fremains;
        this.cancel = cancel;
        this.pair = pair;
        this.price = price;
        this.amount = amount;
        this.time = time;
        this.type = type;
        this.fee = fee;
    }

    public String getId() {
        return id;
    }

    public Double getRemains() {
        return remains;
    }

    public Double getFremains() {
        return fremains;
    }

    public boolean isCancel() {
        return cancel;
    }

    public CexioWebSocketPair getPair() {
        return pair;
    }

    public Double getPrice() {
        return price;
    }

    public Double getAmount() {
        return amount;
    }

    public Date getTime() {
        return time;
    }

    public String getType() {
        return type;
    }

    public Double getFee() {
        return fee;
    }

    @Override
    public String toString() {
        return "CexioWebSocketOrder{"
                + "id='"
                + id
                + '\''
                + ", remains="
                + remains
                + ", fremains="
                + fremains
                + ", cancel="
                + cancel
                + ", pair="
                + pair
                + ", price="
                + price
                + ", amount="
                + amount
                + ", time="
                + time
                + ", type='"
                + type
                + '\''
                + ", fee="
                + fee
                + '}';
    }
}
