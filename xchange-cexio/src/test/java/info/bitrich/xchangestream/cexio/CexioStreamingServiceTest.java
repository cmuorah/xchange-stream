package info.bitrich.xchangestream.cexio;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import info.bitrich.xchangestream.cexio.dto.CexioWebSocketTransaction;
import io.reactivex.observers.TestObserver;
import org.junit.Before;
import org.junit.Test;
import org.knowm.xchange.currency.CurrencyPair;
import org.knowm.xchange.dto.Order;

import java.io.IOException;
import java.time.Instant;
import java.util.Date;

public class CexioStreamingServiceTest {

    private CexioStreamingExchange cexioStreamingExchange;

    @Before
    public void setUp() {
        cexioStreamingExchange = new CexioStreamingExchange();
    }

    @Test
    public void testGetOrderExecution_orderPlace() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode =
                objectMapper.readTree(
                        ClassLoader.getSystemClassLoader().getResourceAsStream("order-place.json"));

        CexioStreamingRawService service = cexioStreamingExchange.getStreamingRawService();

        TestObserver<Order> test = service.getOrderData().test();

        service.handleMessage(jsonNode);

        CexioOrder expected =
                new CexioOrder(
                        Order.OrderType.BID,
                        CurrencyPair.BTC_USD,
                        new Double("0.002"),
                        "5913254239",
                        new Date(1522135708956L),
                        new Double("7176.5"),
                        new Double("0.16"),
                        Order.OrderStatus.NEW);
        test.assertValue(expected);
    }

    @Test
    public void testGetOrderExecution_orderFill() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode =
                objectMapper.readTree(
                        ClassLoader.getSystemClassLoader().getResourceAsStream("order-fill.json"));

        CexioStreamingRawService service = cexioStreamingExchange.getStreamingRawService();

        TestObserver<Order> test = service.getOrderData().test();

        service.handleMessage(jsonNode);

        CexioOrder expected = new CexioOrder(CurrencyPair.BTC_USD, "5891752542", Order.OrderStatus.FILLED, 0d);
        test.assertValue(expected);
    }

    @Test
    public void testGetOrderExecution_orderPartialFill() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode =
                objectMapper.readTree(
                        ClassLoader.getSystemClassLoader().getResourceAsStream("order-partial-fill.json"));

        CexioStreamingRawService service = cexioStreamingExchange.getStreamingRawService();

        TestObserver<Order> test = service.getOrderData().test();

        service.handleMessage(jsonNode);

        CexioOrder expected =
                new CexioOrder(
                        CurrencyPair.BTC_USD,
                        "5891752542",
                        Order.OrderStatus.PARTIALLY_FILLED,
                        new Double("0.002"));
        test.assertValue(expected);
    }

    @Test
    public void testGetOrderExecution_orderCancel() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode =
                objectMapper.readTree(
                        ClassLoader.getSystemClassLoader().getResourceAsStream("order-cancel.json"));

        CexioStreamingRawService service = cexioStreamingExchange.getStreamingRawService();

        TestObserver<Order> test = service.getOrderData().test();

        service.handleMessage(jsonNode);

        CexioOrder expected =
                new CexioOrder(
                        CurrencyPair.BTC_USD,
                        "5891717811",
                        Order.OrderStatus.CANCELED,
                        new Double("0.002"));
        test.assertValue(expected);
    }

    @Test
    public void testGetOrderExecution_invalidJson() {
        CexioStreamingRawService service = cexioStreamingExchange.getStreamingRawService();

        TestObserver<Order> test = service.getOrderData().test();

        service.messageHandler("wrong");

        test.assertError(IOException.class);
    }

    @Test
    public void testGetTransaction_orderPlace() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode =
                objectMapper.readTree(
                        ClassLoader.getSystemClassLoader().getResourceAsStream("transaction-place.json"));

        CexioStreamingRawService service = cexioStreamingExchange.getStreamingRawService();

        TestObserver<CexioWebSocketTransaction> test = service.getTransactions().test();

        service.handleMessage(jsonNode);

        CexioWebSocketTransaction transaction =
                new CexioWebSocketTransaction(
                        "5915157030",
                        "order:5915157028:a:USD",
                        "user:up118134628:a:USD",
                        new Double("0.02"),
                        new Double("16.40"),
                        new Double("35.24"),
                        "up118134628",
                        "USD",
                        null,
                        new Double("-16.40"),
                        5915157028L,
                        null,
                        null,
                        null,
                        "buy",
                        Date.from(Instant.parse("2018-03-27T15:16:52.016Z")),
                        new Double("35.24"),
                        null);

        test.assertValue(transaction);
    }

    @Test
    public void testGetTransaction_orderExecute() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode =
                objectMapper.readTree(
                        ClassLoader.getSystemClassLoader().getResourceAsStream("transaction-exec.json"));

        CexioStreamingRawService service = cexioStreamingExchange.getStreamingRawService();

        TestObserver<CexioWebSocketTransaction> test = service.getTransactions().test();

        service.handleMessage(jsonNode);

        CexioWebSocketTransaction transaction =
                new CexioWebSocketTransaction(
                        "5918682827",
                        "order:5918682821:a:BTC",
                        "user:up118134628:a:BTC",
                        new Double("0.002"),
                        new Double("0"),
                        new Double("0.006"),
                        "up118134628",
                        "BTC",
                        "USD",
                        new Double("0.002"),
                        5918682821L,
                        5918682821L,
                        5918682779L,
                        new Double("8030"),
                        "buy",
                        Date.from(Instant.parse("2018-03-28T05:41:49.482Z")),
                        new Double("0.006"),
                        new Double("0.05"));

        test.assertValue(transaction);
    }
}
