package info.bitrich.xchangestream.gemini;

import com.fasterxml.jackson.databind.JsonNode;
import info.bitrich.xchangestream.service.netty.JsonNettyStreamingService;
import info.bitrich.xchangestream.service.netty.StreamingObjectMapperHelper;
import org.knowm.xchange.currency.CurrencyPair;

/**
 * Created by Lukas Zaoralek on 15.11.17.
 */
public class GeminiProductStreamingService extends JsonNettyStreamingService<JsonNode> {

    private final CurrencyPair currencyPair;

    public GeminiProductStreamingService(String symbolUrl, CurrencyPair currencyPair) {
        super(symbolUrl, Integer.MAX_VALUE, StreamingObjectMapperHelper.SERIALIZER, StreamingObjectMapperHelper.PARSER);
        this.currencyPair = currencyPair;
    }

    @Override
    public boolean processArrayMassageSeparately() {
        return false;
    }

    @Override
    protected String getChannelNameFromMessage(JsonNode message) {
        return currencyPair.toString();
    }

    @Override
    public String getSubscribeMessage(String channelName, Object... args) {
        return null;
    }

    @Override
    public String getUnsubscribeMessage(String channelName) {
        return null;
    }
}
