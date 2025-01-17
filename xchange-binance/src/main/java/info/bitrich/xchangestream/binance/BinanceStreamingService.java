package info.bitrich.xchangestream.binance;

import com.fasterxml.jackson.databind.JsonNode;
import info.bitrich.xchangestream.core.ProductSubscription;
import info.bitrich.xchangestream.service.netty.JsonNettyStreamingService;
import info.bitrich.xchangestream.service.netty.StreamingObjectMapperHelper;

public class BinanceStreamingService extends JsonNettyStreamingService<JsonNode> {

    private final ProductSubscription productSubscription;

    public BinanceStreamingService(String baseUri, ProductSubscription productSubscription) {
        super(baseUri, Integer.MAX_VALUE, StreamingObjectMapperHelper.SERIALIZER, StreamingObjectMapperHelper.PARSER);
        this.productSubscription = productSubscription;
    }

    @Override
    public void messageHandler(String message) {
        super.messageHandler(message);
    }

    @Override
    protected void handleMessage(JsonNode message) {
        super.handleMessage(message);
    }

    @Override
    protected String getChannelNameFromMessage(JsonNode message) {
        return message.get("stream").asText();
    }

    @Override
    public String getSubscribeMessage(String channelName, Object... args) {
        // No op. Disconnecting from the web socket will cancel subscriptions.
        return null;
    }

    @Override
    public String getUnsubscribeMessage(String channelName) {
        // No op. Disconnecting from the web socket will cancel subscriptions.
        return null;
    }

    @Override
    public void sendMessage(String message) {
        // Subscriptions are made upon connection - no messages are sent.
    }

    /**
     * The available subscriptions for this streaming service.
     * @return The subscriptions for the currently open connection.
     */
    public ProductSubscription getProductSubscription() {
        return productSubscription;
    }
}