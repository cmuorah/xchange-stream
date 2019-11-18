package info.bitrich.xchangestream.bitfinex.dto;

import java.util.Objects;

public class BitfinexWebSocketAuthPreTrade {
    private final long id;
    private final String pair;
    private final long mtsCreate;
    private final long orderId;
    private final Double execAmount;
    private final Double execPrice;
    private final String orderType;
    private final Double orderPrice;
    private final long maker;

    public BitfinexWebSocketAuthPreTrade(long id, String pair, long mtsCreate, long orderId,
                                         Double execAmount, Double execPrice,
                                         String orderType, Double orderPrice, long maker) {
        this.id = id;
        this.pair = pair;
        this.mtsCreate = mtsCreate;
        this.orderId = orderId;
        this.execAmount = execAmount;
        this.execPrice = execPrice;
        this.orderType = orderType;
        this.orderPrice = orderPrice;
        this.maker = maker;
    }

    public long getId() {
        return id;
    }

    public String getPair() {
        return pair;
    }

    public long getMtsCreate() {
        return mtsCreate;
    }

    public long getOrderId() {
        return orderId;
    }

    public Double getExecAmount() {
        return execAmount;
    }

    public Double getExecPrice() {
        return execPrice;
    }

    public String getOrderType() {
        return orderType;
    }

    public Double getOrderPrice() {
        return orderPrice;
    }

    public long getMaker() {
        return maker;
    }

    @Override
    public String toString() {
        return "BitfinexWebSocketAuthPreTrade{" +
                "id=" + id +
                ", pair='" + pair + '\'' +
                ", mtsCreate=" + mtsCreate +
                ", orderId=" + orderId +
                ", execAmount=" + execAmount +
                ", execPrice=" + execPrice +
                ", orderType='" + orderType + '\'' +
                ", orderPrice=" + orderPrice +
                ", maker=" + maker +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BitfinexWebSocketAuthPreTrade)) return false;
        BitfinexWebSocketAuthPreTrade that = (BitfinexWebSocketAuthPreTrade) o;
        return getId() == that.getId() &&
                getMtsCreate() == that.getMtsCreate() &&
                getOrderId() == that.getOrderId() &&
                getMaker() == that.getMaker() &&
                Objects.equals(getPair(), that.getPair()) &&
                Objects.equals(getExecAmount(), that.getExecAmount()) &&
                Objects.equals(getExecPrice(), that.getExecPrice()) &&
                Objects.equals(getOrderType(), that.getOrderType()) &&
                Objects.equals(getOrderPrice(), that.getOrderPrice());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getPair(), getMtsCreate(), getOrderId(), getExecAmount(), getExecPrice(), getOrderType(), getOrderPrice(), getMaker());
    }
}
