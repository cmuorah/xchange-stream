package info.bitrich.xchangestream.bankera;

import com.fasterxml.jackson.databind.JsonNode;
import info.bitrich.xchangestream.bankera.dto.BankeraWebSocketSubscriptionMessage;
import info.bitrich.xchangestream.service.netty.JsonNettyStreamingService;
import info.bitrich.xchangestream.service.netty.StreamingObjectMapperHelper;
import io.netty.handler.codec.http.websocketx.extensions.WebSocketClientExtensionHandler;

import java.io.IOException;

public class BankeraStreamingService extends JsonNettyStreamingService<JsonNode> {


    public BankeraStreamingService(String uri) {
        super(uri, Integer.MAX_VALUE, StreamingObjectMapperHelper.SERIALIZER, StreamingObjectMapperHelper.PARSER);
    }

    @Override
    protected String getChannelNameFromMessage(JsonNode message) {
        return message.get("type").asText();
    }

    @Override
    public String getSubscribeMessage(String channelName, Object... args) throws IOException {
        if (args.length != 1)
        	throw new IOException("SubscribeMessage: Insufficient arguments");
        BankeraWebSocketSubscriptionMessage subscribeMessage = new BankeraWebSocketSubscriptionMessage(String.valueOf(args[0]));
        return StreamingObjectMapperHelper.getObjectMapper().writeValueAsString(subscribeMessage);
    }

    @Override
    public String getUnsubscribeMessage(String channelName) throws IOException {
        return null;
    }

    @Override
    protected WebSocketClientExtensionHandler getWebSocketClientExtensionHandler() {
        return null;
    }

}
