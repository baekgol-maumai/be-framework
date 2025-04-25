package ai.maum.beframework.conf.serialization.serializer;

import ai.maum.beframework.codemessage.CodeMessage;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

/**
 * CodeMessage를 문자열로 변환하는 직렬화기
 * @author baekgol@maum.ai
 */
public class CodeMessageSerializer extends JsonSerializer<CodeMessage> {
    @Override
    public void serialize(CodeMessage value, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
//        jsonGenerator.writeString("{\"code\":\"" + value.getCode() + "\",\"message\":\"" + value.getMessage() + "\"}");
        jsonGenerator.writeStartObject();
        jsonGenerator.writeStringField("code", value.getCode());
        jsonGenerator.writeStringField("message", value.getMessage());
        jsonGenerator.writeEndObject();
    }
}
