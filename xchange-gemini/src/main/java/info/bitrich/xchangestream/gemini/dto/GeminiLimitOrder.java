package info.bitrich.xchangestream.gemini.dto;

import org.knowm.xchange.dto.Order;
import org.knowm.xchange.gemini.v1.dto.marketdata.GeminiLevel;


/**
 * Created by Lukas Zaoralek on 15.11.17.
 */
public class GeminiLimitOrder extends GeminiLevel {
    private final Order.OrderType side;

    public GeminiLimitOrder(Order.OrderType side, Double price, Double amount, Double timestamp) {
        super(price, amount, timestamp);
        this.side = side;
    }

    public Order.OrderType getSide() {
        return side;
    }
}
