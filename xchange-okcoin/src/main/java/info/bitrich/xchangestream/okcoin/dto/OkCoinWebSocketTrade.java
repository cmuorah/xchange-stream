package info.bitrich.xchangestream.okcoin.dto;

import org.knowm.xchange.okcoin.dto.marketdata.OkCoinTrade;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class OkCoinWebSocketTrade extends OkCoinTrade {
    public OkCoinWebSocketTrade(String[] items) throws ParseException {
        super(getDate(items[3]).getTime() / 1000, Double.parseDouble(items[1]), Double.parseDouble(items[2]), Long.valueOf(items[0]), items[4]);
    }

    private static Date getDate(String exchangeTime) throws ParseException {
        DateFormat tdf = new SimpleDateFormat("yyyy-MM-dd");
        tdf.setTimeZone(TimeZone.getTimeZone("Hongkong"));
        Date today = Calendar.getInstance(TimeZone.getDefault()).getTime();
        String exchangeToday = tdf.format(today);

        SimpleDateFormat fdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss X");
        fdf.setTimeZone(TimeZone.getDefault());
        return fdf.parse(exchangeToday + " " + exchangeTime + " +0800");
    }
}
