package info.bitrich.xchangestream.service.netty;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.function.Function;

public abstract class JsonNettyStreamingService<T> extends NettyStreamingService<T> {
    private static final Logger LOG = LoggerFactory.getLogger(JsonNettyStreamingService.class);
    private final Function<T, String> serializer;
    private final Function<String, T> parser;

    public JsonNettyStreamingService(String apiUrl, Function<T, String> serializer, Function<String, T> parser) {
        super(apiUrl);
        this.serializer = serializer;
        this.parser = parser;
    }

    public JsonNettyStreamingService(String apiUrl, int maxFramePayloadLength, Function<T, String> serializer, Function<String, T> parser) {
        super(apiUrl, maxFramePayloadLength);
        this.serializer = serializer;
        this.parser = parser;
    }

    public JsonNettyStreamingService(String apiUrl, int maxFramePayloadLength, Duration connectionTimeout, Duration retryDuration, int idleTimeoutSeconds, Function<T, String> serializer, Function<String, T> parser) {
        super(apiUrl, maxFramePayloadLength, connectionTimeout, retryDuration, idleTimeoutSeconds);
        this.serializer = serializer;
        this.parser = parser;
    }

    public boolean processArrayMassageSeparately() {
        return true;
    }

    @Override
    public void messageHandler(String message) {
        LOG.debug("Received message: {}", message);
        T jsonNode;

        // Parse incoming message to JSON
        try {
            jsonNode = parser.apply(message);
            handleMessage(jsonNode);
        } catch (Exception e) {
            LOG.error("Error parsing incoming message to JSON: {}", message);
        }
    }

    protected void sendObjectMessage(T message) {
        try {
            sendMessage(serializer.apply(message));
        } catch (Exception e) {
            LOG.error("Error creating json message: {}", e.getMessage());
        }
    }
}
