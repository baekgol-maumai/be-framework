package ai.maum.beframework.conf.serialization.deserializer;

import ai.maum.beframework.codemessage.CodeMessage;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;
import java.util.Optional;

/**
 * 문자열을 CodeMessage로 변환하는 역직렬화기
 * @author baekgol@maum.ai
 */
public class CodeMessageDeserializer extends JsonDeserializer<CodeMessage> {
    @Override
    public CodeMessage deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
        final JsonNode node = jsonParser.getCodec().readTree(jsonParser);

        if(node == null)
            return null;

        return new CodeMessage() {
            @Override
            public String getCode() {
                return Optional.ofNullable(node.get("code"))
                        .map(JsonNode::asText)
                        .orElse(null);
            }

            @Override
            public String getMessage() {
                return Optional.ofNullable(node.get("message"))
                        .map(JsonNode::asText)
                        .orElse(null);
            }
        };
    }
}
