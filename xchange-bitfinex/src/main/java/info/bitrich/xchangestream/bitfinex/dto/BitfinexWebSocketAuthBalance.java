package info.bitrich.xchangestream.bitfinex.dto;

import java.util.Objects;

public class BitfinexWebSocketAuthBalance {
    private String walletType;
    private String currency;
    private Double balance;
    private Double unsettledInterest;
    private Double balanceAvailable;

    public BitfinexWebSocketAuthBalance(String walletType, String currency, Double balance,
                                        Double unsettledInterest, Double balanceAvailable) {
        this.walletType = walletType;
        this.currency = currency;
        this.balance = balance;
        this.unsettledInterest = unsettledInterest;
        this.balanceAvailable = balanceAvailable;
    }

    public String getWalletType() {
        return walletType;
    }

    public String getCurrency() {
        return currency;
    }

    public Double getBalance() {
        return balance;
    }

    public Double getUnsettledInterest() {
        return unsettledInterest;
    }

    public Double getBalanceAvailable() {
        return balanceAvailable;
    }

    @Override
    public String toString() {
        return "BitfinexWebSocketAuthBalance{" +
                "walletType='" + walletType + '\'' +
                ", currency='" + currency + '\'' +
                ", balance=" + balance +
                ", unsettledInterest=" + unsettledInterest +
                ", balanceAvailable=" + balanceAvailable +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BitfinexWebSocketAuthBalance)) return false;
        BitfinexWebSocketAuthBalance that = (BitfinexWebSocketAuthBalance) o;
        return Objects.equals(walletType, that.walletType) &&
                Objects.equals(currency, that.currency) &&
                Objects.equals(balance, that.balance) &&
                Objects.equals(unsettledInterest, that.unsettledInterest) &&
                Objects.equals(balanceAvailable, that.balanceAvailable);
    }

    @Override
    public int hashCode() {

        return Objects.hash(walletType, currency, balance, unsettledInterest, balanceAvailable);
    }
}
