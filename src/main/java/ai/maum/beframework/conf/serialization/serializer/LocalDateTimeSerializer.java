package ai.maum.beframework.conf.serialization.serializer;

import ai.maum.beframework.util.WebSocketClientUtil;
import ai.maum.beframework.vo.BaseException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * LocalDateTime을 날짜 및 시간 형식의 문자열로 직렬화하는 Serializer
 * @author baekgol@maum.ai
 */
public class LocalDateTimeSerializer extends JsonSerializer<LocalDateTime> {
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Override
    public void serialize(LocalDateTime value, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) {
        try {
            jsonGenerator.writeString(value.format(formatter));
        } catch(IOException e) {
            final BaseException ex = BaseException.of(e);
            WebSocketClientUtil.sendError(ex);
            throw ex;
        }
    }
}
