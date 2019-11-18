package info.bitrich.xchangestream.bitflyer.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.knowm.xchange.currency.CurrencyPair;
import org.knowm.xchange.dto.Order;
import org.knowm.xchange.dto.trade.LimitOrder;


/**
 * Created by Lukas Zaoralek on 14.11.17.
 */
public class BitflyerLimitOrder {
    private final Double price;
    private final Double size;

    public BitflyerLimitOrder(@JsonProperty("price") Double price,
                              @JsonProperty("size") Double size) {
        this.price = price;
        this.size = size;
    }

    public Double getPrice() {
        return price;
    }

    public Double getSize() {
        return size;
    }

    public LimitOrder toLimitOrder(CurrencyPair pair, Order.OrderType side) {
        return new LimitOrder(side, size, pair, "", null, price);
    }
}
