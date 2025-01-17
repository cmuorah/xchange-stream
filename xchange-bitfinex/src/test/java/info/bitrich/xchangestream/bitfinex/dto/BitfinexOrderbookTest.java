package info.bitrich.xchangestream.bitfinex.dto;

import org.junit.Test;
import org.knowm.xchange.bitfinex.service.BitfinexAdapters;
import org.knowm.xchange.bitfinex.v1.dto.marketdata.BitfinexDepth;
import org.knowm.xchange.dto.marketdata.OrderBook;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.knowm.xchange.currency.CurrencyPair.BTC_USD;

public class BitfinexOrderbookTest {

    @Test
    public void timestampShouldBeInSeconds() {
        BitfinexDepth depth = new BitfinexOrderbook(new BitfinexOrderbookLevel[]{
                new BitfinexOrderbookLevel(1d, 1d, 1d),
                new BitfinexOrderbookLevel(1d, 1d, 1d)
        }).toBitfinexDepth();

        OrderBook orderBook = BitfinexAdapters.adaptOrderBook(depth, BTC_USD);

        // What is the time now... after order books created?
        assertThat("The timestamp should be a value less than now, but was: " + orderBook.getTimeStamp(),
                !(orderBook.getTimeStamp() > System.currentTimeMillis()));
    }
}