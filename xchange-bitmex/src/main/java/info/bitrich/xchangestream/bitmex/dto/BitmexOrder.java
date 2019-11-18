package info.bitrich.xchangestream.bitmex.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.knowm.xchange.currency.CurrencyPair;
import org.knowm.xchange.dto.Order;
import org.knowm.xchange.dto.trade.LimitOrder;
import org.knowm.xchange.dto.trade.MarketOrder;


public class BitmexOrder extends BitmexMarketDataEvent {

    private String orderID;
    private int account;
    private String side;
    private Double price;
    private Double avgPx;
    private String ordType;
    private OrderStatus ordStatus;
    private String clOrdID;
    private Double orderQty;
    private Double cumQty;
    private boolean workingIndicator;

    @JsonCreator
    public BitmexOrder(@JsonProperty("symbol") String symbol,
                       @JsonProperty("timestamp") String timestamp,
                       @JsonProperty("orderID") String orderID,
                       @JsonProperty("account") int account,
                       @JsonProperty("side") String side,
                       @JsonProperty("price") Double price,
                       @JsonProperty("avgPx") Double avgPx,
                       @JsonProperty("ordType") String ordType,
                       @JsonProperty("ordStatus") String ordStatus,
                       @JsonProperty("clOrdID") String clOrdID,
                       @JsonProperty("orderQty") Double orderQty,
                       @JsonProperty("cumQty") Double cumQty,
                       @JsonProperty("workingIndicator") boolean workingIndicator) {
        super(symbol, timestamp);
        this.orderID = orderID;
        this.account = account;
        this.side = side;
        this.price = price;
        this.avgPx = avgPx;
        this.ordType = ordType;
        try {
            this.ordStatus = OrderStatus.valueOf(ordStatus.toUpperCase());
        } catch (Exception e) {
            this.ordStatus = OrderStatus.UNKNOW;
        }
        this.clOrdID = clOrdID;
        this.orderQty = orderQty;
        this.cumQty = cumQty;
        this.workingIndicator = workingIndicator;
    }

    public boolean isNotWorkingIndicator() {
        return !workingIndicator;
    }

    public Order toOrder() {
        Order.Builder order;
        if (ordType.equals("Market")) {
            order = new MarketOrder.Builder(side.equals("Buy") ? Order.OrderType.BID : Order.OrderType.ASK, new CurrencyPair(symbol.substring(0, 3), symbol.substring(3, symbol.length())));
        } else {
            order = new LimitOrder.Builder(side.equals("Buy") ? Order.OrderType.BID : Order.OrderType.ASK, new CurrencyPair(symbol.substring(0, 3), symbol.substring(3, symbol.length())));
        }
        order.id(orderID)
                .averagePrice(avgPx)
                .originalAmount(orderQty)
                .cumulativeAmount(cumQty);

        switch (ordStatus) {
            case NEW:
                order.orderStatus(Order.OrderStatus.NEW);
                break;
            case PARTIALLYFILLED:
                order.orderStatus(Order.OrderStatus.PARTIALLY_FILLED);
                break;
            case FILLED:
                order.orderStatus(Order.OrderStatus.FILLED);
                break;
            case TBD:
                order.orderStatus(Order.OrderStatus.PENDING_CANCEL);
                break;
            case CANCELED:
                order.orderStatus(Order.OrderStatus.CANCELED);
                break;
            case REJECTED:
                order.orderStatus(Order.OrderStatus.REJECTED);
            default:
                order.orderStatus(Order.OrderStatus.UNKNOWN);
                break;
        }
        if (ordType.equals("Market")) {
            return ((MarketOrder.Builder) order).build();
        } else {
            return ((LimitOrder.Builder) order).build();
        }
    }

    public String getOrderID() {
        return orderID;
    }

    public int getAccount() {
        return account;
    }

    public String getSide() {
        return side;
    }

    public Double getPrice() {
        return price;
    }

    public Double getAvgPx() {
        return avgPx;
    }

    public String getOrdType() {
        return ordType;
    }

    public OrderStatus getOrdStatus() {
        return ordStatus;
    }

    public String getClOrdID() {
        return clOrdID;
    }

    public Double getOrderQty() {
        return orderQty;
    }

    public Double getCumQty() {
        return cumQty;
    }

    public boolean isWorkingIndicator() {
        return workingIndicator;
    }

    public enum OrderStatus {
        NEW,
        PARTIALLYFILLED,
        FILLED,
        TBD,
        CANCELED,
        REJECTED,
        UNKNOW
    }
}
