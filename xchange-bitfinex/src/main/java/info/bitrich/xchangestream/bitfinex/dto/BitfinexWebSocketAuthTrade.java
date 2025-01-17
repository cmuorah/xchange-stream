package info.bitrich.xchangestream.bitfinex.dto;

import java.util.Objects;

public class BitfinexWebSocketAuthTrade extends BitfinexWebSocketAuthPreTrade {
    private final Double fee;
    private final String feeCurrency;

    public BitfinexWebSocketAuthTrade(long id, String pair, long mtsCreate,
                                      long orderId, Double execAmount, Double execPrice,
                                      String orderType, Double orderPrice,
                                      long maker, Double fee, String feeCurrency) {
        super(id, pair, mtsCreate, orderId, execAmount, execPrice, orderType, orderPrice, maker);
        this.fee = fee;
        this.feeCurrency = feeCurrency;
    }

    public Double getFee() {
        return fee;
    }

    public String getFeeCurrency() {
        return feeCurrency;
    }

    @Override
    public String toString() {
        return "BitfinexWebSocketAuthenticatedTrade{" +
                "id=" + getId() +
                ", pair='" + getPair() + '\'' +
                ", mtsCreate=" + getMtsCreate() +
                ", orderId=" + getOrderId() +
                ", execAmount=" + getExecAmount() +
                ", execPrice=" + getExecPrice() +
                ", orderType='" + getOrderType() + '\'' +
                ", orderPrice=" + getOrderPrice() +
                ", maker=" + getMtsCreate() +
                ", fee=" + fee +
                ", feeCurrency='" + feeCurrency + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BitfinexWebSocketAuthTrade)) return false;
        if (!super.equals(o)) return false;
        BitfinexWebSocketAuthTrade that = (BitfinexWebSocketAuthTrade) o;
        return Objects.equals(fee, that.fee) &&
                Objects.equals(feeCurrency, that.feeCurrency);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), fee, feeCurrency);
    }
}
