package info.bitrich.xchangestream.service.netty;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.openhft.chronicle.core.Jvm;

import java.util.function.Function;

/**
 * This class should be merged with ObjectMapperHelper from XStream..
 *
 * @author Nikita Belenkiy on 19/06/2018.
 */
public class StreamingObjectMapperHelper {

    private static final ObjectMapper objectMapper;

    static {
        objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_DEFAULT);
    }

    private StreamingObjectMapperHelper() {

    }

    public static ObjectMapper getObjectMapper() {
        return objectMapper;
    }

    public static final Function<JsonNode, String> SERIALIZER = o -> {
        try {
            return objectMapper.writeValueAsString(o);
        } catch (JsonProcessingException e) {
            Jvm.rethrow(e);
        }
        return null;
    };

    public static final Function<String, JsonNode> PARSER = m -> {
        try {
            return objectMapper.readTree(m);
        } catch (Exception e) {
            Jvm.rethrow(e);
        }
        return null;
    };
}
