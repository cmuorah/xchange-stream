package info.bitrich.xchangestream.poloniex2.dto;


/**
 * Created by Lukas Zaoralek on 11.11.17.
 */
public class OrderbookModifiedEvent {
    private String type;
    private Double price;
    private Double volume;

    public OrderbookModifiedEvent(String type, Double price, Double volume) {
        this.type = type;
        this.price = price;
        this.volume = volume;
    }

    public String getType() {
        return type;
    }

    public Double getPrice() {
        return price;
    }

    public Double getVolume() {
        return volume;
    }
}
