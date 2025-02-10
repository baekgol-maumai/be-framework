package ai.maum.beframework.conf.serialization.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/**
 * LocalTime을 시간 형식의 문자열로 변환하는 직렬화기
 * @author baekgol@maum.ai
 */
public class LocalTimeSerializer extends JsonSerializer<LocalTime> {
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");

    @Override
    public void serialize(LocalTime value, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeString(value.format(formatter));
    }
}
