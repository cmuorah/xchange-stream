package info.bitrich.xchangestream.cexio;

import org.knowm.xchange.currency.CurrencyPair;
import org.knowm.xchange.dto.trade.LimitOrder;

import java.util.Date;

public class CexioOrder extends LimitOrder {

    private Double remainingAmount;

    public CexioOrder(
            OrderType type,
            CurrencyPair currencyPair,
            Double originalAmount,
            String id,
            Date timestamp,
            Double limitPrice,
            Double fee,
            OrderStatus status) {
        super(type, originalAmount, currencyPair, id, timestamp.getTime(), limitPrice, null, null, fee, status);
        this.remainingAmount = null;
    }

    public CexioOrder(
            CurrencyPair currencyPair, String id, OrderStatus status, Double remainingAmount) {
        this(null, currencyPair, null, id, null, null, null, status);
        this.remainingAmount = remainingAmount;
    }

    @Override
    public Double getRemainingAmount() {
        if (remainingAmount != null) {
            return remainingAmount;
        }

        return super.getRemainingAmount();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CexioOrder)) return false;
        if (!super.equals(o)) return false;

        CexioOrder that = (CexioOrder) o;

        return remainingAmount != null
                ? remainingAmount.compareTo(that.remainingAmount) == 0
                : that.remainingAmount == null;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (remainingAmount != null ? remainingAmount.hashCode() : 0);
        return result;
    }
}
