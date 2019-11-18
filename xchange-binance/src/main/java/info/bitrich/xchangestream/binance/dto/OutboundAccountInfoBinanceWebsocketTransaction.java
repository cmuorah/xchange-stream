package info.bitrich.xchangestream.binance.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;
import java.util.List;

public class OutboundAccountInfoBinanceWebsocketTransaction extends BaseBinanceWebSocketTransaction {

    private final Double makerCommissionRate;
    private final Double takerCommissionRate;
    private final Double buyerCommissionRate;
    private final Double sellerCommissionRate;
    private final boolean canTrade;
    private final boolean canWithdraw;
    private final boolean canDeposit;
    private final long lastUpdateTimestamp;
    private final List<BinanceWebsocketBalance> balances;

    public OutboundAccountInfoBinanceWebsocketTransaction(
            @JsonProperty("e") String eventType,
            @JsonProperty("E") String eventTime,
            @JsonProperty("m") Double makerCommissionRate,
            @JsonProperty("t") Double takerCommissionRate,
            @JsonProperty("b") Double buyerCommissionRate,
            @JsonProperty("s") Double sellerCommissionRate,
            @JsonProperty("T") boolean canTrade,
            @JsonProperty("W") boolean canWithdraw,
            @JsonProperty("D") boolean canDeposit,
            @JsonProperty("u") long lastUpdateTimestamp,
            @JsonProperty("B") List<BinanceWebsocketBalance> balances)
    {
        super(eventType, eventTime);
        this.makerCommissionRate = makerCommissionRate;
        this.takerCommissionRate = takerCommissionRate;
        this.buyerCommissionRate = buyerCommissionRate;
        this.sellerCommissionRate = sellerCommissionRate;
        this.canTrade = canTrade;
        this.canWithdraw = canWithdraw;
        this.canDeposit = canDeposit;
        this.lastUpdateTimestamp = lastUpdateTimestamp;
        this.balances = balances;
    }

    public Double getMakerCommissionRate() {
        return makerCommissionRate;
    }

    public Double getTakerCommissionRate() {
        return takerCommissionRate;
    }

    public Double getBuyerCommissionRate() {
        return buyerCommissionRate;
    }

    public Double getSellerCommissionRate() {
        return sellerCommissionRate;
    }

    public boolean isCanTrade() {
        return canTrade;
    }

    public boolean isCanWithdraw() {
        return canWithdraw;
    }

    public boolean isCanDeposit() {
        return canDeposit;
    }

    public long getLastUpdateTimestamp() {
        return lastUpdateTimestamp;
    }

    public List<BinanceWebsocketBalance> getBalances() {
        return balances;
    }

    @Override
    public String toString() {
        return "OutboundAccountInfoBinanceWebsocketTransaction [makerCommissionRate=" + makerCommissionRate
                + ", takerCommissionRate=" + takerCommissionRate + ", buyerCommissionRate=" + buyerCommissionRate
                + ", sellerCommissionRate=" + sellerCommissionRate + ", canTrade=" + canTrade + ", canWithdraw="
                + canWithdraw + ", canDeposit=" + canDeposit + ", lastUpdateTimestamp=" + lastUpdateTimestamp
                + ", balances=" + balances + "]";
    }
}
