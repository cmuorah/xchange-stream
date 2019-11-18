package info.bitrich.xchangestream.bitmex.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.knowm.xchange.currency.CurrencyPair;
import org.knowm.xchange.dto.Order;
import org.knowm.xchange.dto.trade.LimitOrder;


/**
 * Created by Lukas Zaoralek on 13.11.17.
 */
public class BitmexLimitOrder extends BitmexMarketDataEvent {
    public static final String ASK_SIDE = "Sell";
    public static final String BID_SIDE = "Buy";

    protected final String id;
    protected final String side;
    protected final Double price;
    protected final Double size;

    @JsonCreator
    public BitmexLimitOrder(@JsonProperty("symbol") String symbol,
                            @JsonProperty("id") String id,
                            @JsonProperty("side") String side,
                            @JsonProperty("price") Double price,
                            @JsonProperty("size") Double size) {
        super(symbol, null);
        this.id = id;
        this.side = side;
        this.price = price;
        this.size = size;
    }

    public BitmexLimitOrder(String symbol,
                            String id,
                            String side,
                            Double price,
                            Double size,
                            String timestamp) {
        super(symbol, timestamp);
        this.id = id;
        this.side = side;
        this.price = price;
        this.size = size;
    }

    public String getId() {
        return id;
    }

    public String getSide() {
        return side;
    }

    public Double getPrice() {
        return price;
    }

    public Double getSize() {
        return size;
    }

    public Order.OrderType getOrderSide() {
        return side.equals(ASK_SIDE) ? Order.OrderType.ASK : Order.OrderType.BID;
    }

    public LimitOrder toLimitOrder() {
        CurrencyPair pair = getCurrencyPair();
        Order.OrderType orderType = getOrderSide();
        return new LimitOrder(orderType, size, pair, id, null, price);
    }
}
