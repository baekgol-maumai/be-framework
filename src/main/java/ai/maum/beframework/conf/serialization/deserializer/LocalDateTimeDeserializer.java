package ai.maum.beframework.conf.serialization.deserializer;

import ai.maum.beframework.codemessage.SystemCodeMsg;
import ai.maum.beframework.util.WebSocketClientUtil;
import ai.maum.beframework.vo.BaseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Slf4j
public class LocalDateTimeDeserializer extends JsonDeserializer<LocalDateTime> {
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Override
    public LocalDateTime deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) {
        JsonNode node;

        try {
            node = jsonParser.getCodec().readTree(jsonParser);
        } catch(IOException e) {
            final BaseException ex = BaseException.of(SystemCodeMsg.CONVERT_FAILURE);
            WebSocketClientUtil.sendError(ex);
            throw ex;
        }

        return LocalDateTime.parse(node.asText(), formatter);
    }
}
