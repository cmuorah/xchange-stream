package info.bitrich.xchangestream.binance.dto;


public class BinanceRawTrade {
    private final String eventType;
    private final String eventTime;
    private final String symbol;
    private final long tradeId;
    private final Double price;
    private final Double quantity;
    private final long buyerOrderId;
    private final long sellerOrderId;
    private final long timestamp;
    private final boolean buyerMarketMaker;
    private final boolean ignore;

    public BinanceRawTrade(String eventType, String eventTime, String symbol, long tradeId, Double price,
                           Double quantity, long buyerOrderId, long sellerOrderId, long timestamp,
                           boolean buyerMarketMaker, boolean ignore) {
        this.eventType = eventType;
        this.eventTime = eventTime;
        this.symbol = symbol;
        this.tradeId = tradeId;
        this.price = price;
        this.quantity = quantity;
        this.buyerOrderId = buyerOrderId;
        this.sellerOrderId = sellerOrderId;
        this.timestamp = timestamp;
        this.buyerMarketMaker = buyerMarketMaker;
        this.ignore = ignore;
    }

    public String getEventType() {
        return eventType;
    }

    public String getEventTime() {
        return eventTime;
    }

    public String getSymbol() {
        return symbol;
    }

    public long getTradeId() {
        return tradeId;
    }

    public Double getPrice() {
        return price;
    }

    public Double getQuantity() {
        return quantity;
    }

    public long getBuyerOrderId() {
        return buyerOrderId;
    }

    public long getSellerOrderId() {
        return sellerOrderId;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public boolean isBuyerMarketMaker() {
        return buyerMarketMaker;
    }

    public boolean isIgnore() {
        return ignore;
    }
}
