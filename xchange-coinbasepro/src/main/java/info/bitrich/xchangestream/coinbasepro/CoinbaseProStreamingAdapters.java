package info.bitrich.xchangestream.coinbasepro;

import info.bitrich.xchangestream.coinbasepro.dto.CoinbaseProWebSocketTransaction;
import org.knowm.xchange.coinbasepro.CoinbaseProAdapters;
import org.knowm.xchange.coinbasepro.dto.trade.CoinbaseProOrder;
import org.knowm.xchange.currency.CurrencyPair;
import org.knowm.xchange.dto.Order;
import org.knowm.xchange.dto.Order.OrderStatus;
import org.knowm.xchange.dto.Order.OrderType;
import org.knowm.xchange.dto.trade.LimitOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.TextStyle;
import java.time.temporal.ChronoField;
import java.time.temporal.TemporalField;
import java.util.Date;
import java.util.TimeZone;

public class CoinbaseProStreamingAdapters {

    private static final Logger LOG = LoggerFactory.getLogger(CoinbaseProStreamingAdapters.class);
    private static final DateTimeFormatter formatter = new DateTimeFormatterBuilder()
            .appendPattern("yyyy-MM-dd'T'HH:mm:ss")
            .appendFraction(ChronoField.MICRO_OF_SECOND, 0, 9, true) // Nanoseconds = 0-9 digits of fractional second.
            .appendLiteral('Z')
            .toFormatter();


    /**
     * TODO this clearly isn't good enough. We need an initial snapshot that these
     * can build on.
     */
    static Order adaptOrder(CoinbaseProWebSocketTransaction s) {
        switch (s.getType()) {
            case "activate":
            case "received":
                return CoinbaseProAdapters.adaptOrder(
                        new CoinbaseProOrder(
                                s.getOrderId(),
                                s.getPrice(),
                                s.getSize() == null ? 0d : s.getSize(),
                                s.getProductId(),
                                s.getSide(),
                                s.getTime(), //createdAt,
                                null, //doneAt,
                                0d, // filled size
                                null, // fees
                                s.getType(), // status - TODO no clean mapping atm
                                false, // settled
                                s.getType().equals("received") ? "limit" : "stop", // type. TODO market orders
                                null, // doneReason
                                null,
                                null, // stop TODO no source for this
                                null // stopPrice
                        )
                );
            default:
                OrderType type = s.getSide().equals("buy") ? OrderType.BID : OrderType.ASK;
                CurrencyPair currencyPair = new CurrencyPair(s.getProductId().replace('-', '/'));
                return new LimitOrder.Builder(type, currencyPair)
                        .id(s.getOrderId())
                        .orderStatus(adaptOrderStatus(s))
                        .build();
        }
    }


    private static OrderStatus adaptOrderStatus(CoinbaseProWebSocketTransaction s) {
        if (s.getType().equals("done")) {
            if (s.getReason().equals("canceled")) {
                return OrderStatus.CANCELED;
            } else {
                return OrderStatus.FILLED;
            }
        } else if (s.getType().equals("match")) {
            return OrderStatus.PARTIALLY_FILLED;
        } else {
            return OrderStatus.NEW;
        }
    }

    public static Long parseDate(final String rawDate) {

        try {
            return LocalDateTime.parse(rawDate, formatter).toInstant(ZoneOffset.UTC).toEpochMilli();
        } catch (Exception e) {
            LOG.warn("unable to parse rawDate={}", rawDate, e);
            return null;
        }
    }

    public static void main(String[] args) {
        String s = "2014-11-07T08:19:28.464459Z";

        System.out.println(formatter.format(LocalDateTime.now()));
        System.out.println(LocalDateTime.parse(s, formatter).toInstant(ZoneOffset.UTC).toEpochMilli());
    }
}