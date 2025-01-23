package ai.maum.beframework.conf.serialization.deserializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/**
 * 시간 형식의 문자열을 LocalTime으로 역직렬화하는 Deserializer
 * @author baekgol@maum.ai
 */
public class LocalTimeDeserializer extends JsonDeserializer<LocalTime> {
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");

    @Override
    public LocalTime deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
        return LocalTime.parse(jsonParser.getText(), formatter);
    }
}
