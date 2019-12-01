package info.bitrich.xchangestream.coinbasepro;


import com.jsoniter.JsonIterator;
import com.jsoniter.any.Any;
import com.jsoniter.output.JsonStream;
import info.bitrich.xchangestream.coinbasepro.dto.CoinbaseProWebSocketSubscriptionMessage;
import info.bitrich.xchangestream.coinbasepro.dto.CoinbaseProWebSocketTransaction;
import info.bitrich.xchangestream.coinbasepro.netty.WebSocketClientCompressionAllowClientNoContextHandler;
import info.bitrich.xchangestream.core.ProductSubscription;
import info.bitrich.xchangestream.service.netty.JsonNettyStreamingService;
import info.bitrich.xchangestream.service.netty.WebSocketClientHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.websocketx.WebSocketClientHandshaker;
import io.netty.handler.codec.http.websocketx.extensions.WebSocketClientExtensionHandler;
import io.reactivex.Observable;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import org.knowm.xchange.coinbasepro.dto.account.CoinbaseProWebsocketAuthData;
import org.knowm.xchange.currency.CurrencyPair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.function.Supplier;

import static io.netty.util.internal.StringUtil.isNullOrEmpty;

public class CoinbaseProStreamingService extends JsonNettyStreamingService<Any> {
    private static final Logger LOG = LoggerFactory.getLogger(CoinbaseProStreamingService.class);
    private static final String SUBSCRIBE = "subscribe";
    private static final String UNSUBSCRIBE = "unsubscribe";
    private static final String SHARE_CHANNEL_NAME = "ALL";
    private final Map<String, Observable<Any>> subscriptions = new Object2ObjectOpenHashMap<>();
    private final Supplier<CoinbaseProWebsocketAuthData> authData;
    private ProductSubscription product = null;
    private WebSocketClientHandler.WebSocketMessageHandler channelInactiveHandler = null;

    public CoinbaseProStreamingService(String apiUrl, Supplier<CoinbaseProWebsocketAuthData> authData) {
        super(apiUrl, Integer.MAX_VALUE, JsonStream::serialize, JsonIterator::deserialize);
        this.authData = authData;
    }

    public ProductSubscription getProduct() {
        return product;
    }

    @Override
    public String getSubscriptionUniqueId(String channelName, Object... args) {
        return SHARE_CHANNEL_NAME;
    }

    /**
     * Subscribes to the provided channel name, maintains a cache of subscriptions, in order not to
     * subscribe more than once to the same channel.
     *
     * @param channelName the name of the requested channel.
     * @return an Observable of json objects coming from the exchange.
     */
    @Override
    public Observable<Any> subscribeChannel(String channelName, Object... args) {
        channelName = SHARE_CHANNEL_NAME;

        if (!channels.containsKey(channelName) && !subscriptions.containsKey(channelName)) {
            subscriptions.put(channelName, super.subscribeChannel(channelName, args));
        }

        return subscriptions.get(channelName);
    }

    /**
     * Subscribes to web socket transactions related to the specified currency, in their raw format.
     *
     * @param currencyPair The currency pair.
     * @return The stream.
     */
    public Observable<CoinbaseProWebSocketTransaction> getRawWebSocketTransactions(CurrencyPair currencyPair, boolean filterChannelName) {
        String channelName = currencyPair.base.toString() + "-" + currencyPair.counter.toString();
        return subscribeChannel(channelName)
                .map(s -> s.as(CoinbaseProWebSocketTransaction.class))
                .filter(t -> channelName.equals(t.getProductId()))
                .filter(t -> !isNullOrEmpty(t.getType()));
    }

    boolean isAuthenticated() {
        return authData.get() != null;
    }

    @Override
    protected String getChannelNameFromMessage(Any message) {
        return SHARE_CHANNEL_NAME;
    }

    @Override
    public String getSubscribeMessage(String channelName, Object... args) {
        CoinbaseProWebSocketSubscriptionMessage subscribeMessage = new CoinbaseProWebSocketSubscriptionMessage(SUBSCRIBE, product, authData.get());
        return JsonStream.serialize(subscribeMessage);
    }

    @Override
    public String getUnsubscribeMessage(String channelName) {
        CoinbaseProWebSocketSubscriptionMessage subscribeMessage = new CoinbaseProWebSocketSubscriptionMessage(UNSUBSCRIBE, new String[]{"level2", "matches", "ticker"}, authData.get());
        return JsonStream.serialize(subscribeMessage);
    }

    @Override
    protected void handleMessage(Any message) {
        super.handleMessage(message);
    }

    @Override
    protected WebSocketClientExtensionHandler getWebSocketClientExtensionHandler() {
        return WebSocketClientCompressionAllowClientNoContextHandler.INSTANCE;
    }

    @Override
    protected WebSocketClientHandler getWebSocketClientHandler(WebSocketClientHandshaker handshaker,
                                                               WebSocketClientHandler.WebSocketMessageHandler handler) {
        LOG.info("Registering CoinbaseProWebSocketClientHandler");
        return new CoinbaseProWebSocketClientHandler(handshaker, handler);
    }

    public void setChannelInactiveHandler(WebSocketClientHandler.WebSocketMessageHandler channelInactiveHandler) {
        this.channelInactiveHandler = channelInactiveHandler;
    }

    public void subscribeMultipleCurrencyPairs(ProductSubscription... products) {
        this.product = products[0];
    }

    /**
     * Custom client handler in order to execute an external, user-provided handler on channel events.
     * This is useful because it seems CoinbasePro unexpectedly closes the web socket connection.
     */
    class CoinbaseProWebSocketClientHandler extends NettyWebSocketClientHandler {

        public CoinbaseProWebSocketClientHandler(WebSocketClientHandshaker handshaker, WebSocketMessageHandler handler) {
            super(handshaker, handler);
        }

        @Override
        public void channelActive(ChannelHandlerContext ctx) {
            super.channelActive(ctx);
        }

        @Override
        public void channelInactive(ChannelHandlerContext ctx) {
            super.channelInactive(ctx);
            if (channelInactiveHandler != null) {
                channelInactiveHandler.onMessage("WebSocket Client disconnected!");
            }
        }
    }
}
