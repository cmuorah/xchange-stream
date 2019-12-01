package info.bitrich.xchangestream.coinbasepro.dto;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jsoniter.JsonIterator;
import com.jsoniter.output.JsonStream;
import info.bitrich.xchangestream.core.ProductSubscription;
import org.junit.Assert;
import org.junit.Test;
import org.knowm.xchange.currency.CurrencyPair;

/**
 * Created by luca on 5/3/17.
 */
public class CoinbaseProWebSocketSubscriptionMessageTest {

    @Test
    public void testWebSocketMessageSerialization() throws JsonProcessingException {

        ProductSubscription productSubscription = ProductSubscription.create().addOrderbook(CurrencyPair.BTC_USD)
                .addTrades(CurrencyPair.BTC_USD).addTicker(CurrencyPair.BTC_USD).build();
        CoinbaseProWebSocketSubscriptionMessage message = new CoinbaseProWebSocketSubscriptionMessage("subscribe", productSubscription, null);


        String serialized = JsonStream.serialize(message);

        Assert.assertEquals("{\"type\":\"subscribe\",\"channels\":[{\"name\":\"matches\",\"product_ids\":[\"BTC-USD\"]},{\"name\":\"ticker\",\"product_ids\":[\"BTC-USD\"]},{\"name\":\"level2\",\"product_ids\":[\"BTC-USD\"]}]}", serialized);
    }
}
