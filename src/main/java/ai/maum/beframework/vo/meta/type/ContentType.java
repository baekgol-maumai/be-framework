package ai.maum.beframework.vo.meta.type;

import ai.maum.beframework.codemessage.SystemCodeMsg;
import ai.maum.beframework.vo.BaseException;
import ai.maum.beframework.vo.meta.Type;
import ai.maum.beframework.vo.meta.type.binary.BinaryType;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

/**
 * 콘텐츠 유형
 * @author baekgol@maum.ai
 */
@Getter
public class ContentType implements Type {
    private final DataType dataType;

    @RequiredArgsConstructor
    public enum Scheme {
        TEXT(TextType.class),
        BINARY(BinaryType.class);

        private final Class<? extends DataType> clazz;
    }

    private ContentType(DataType dataType) {
        this.dataType = dataType;
    }

    public static ContentType of(DataType dataType) {
        return new ContentType(dataType);
    }

    @JsonValue
    public DataType serialize() {
        return this.dataType;
    }

    @JsonCreator
    public static ContentType from(JsonNode node) {
        final JsonNode schemeNode = node.get("scheme");
        final JsonNode formatNode = node.get("format");
        final JsonNode specNode = node.get("spec");

        if(schemeNode == null || formatNode == null)
            throw BaseException.of(SystemCodeMsg.PARAM_WRONG);

        return Arrays.stream(Scheme.values())
                .filter(e -> e.name().equals(schemeNode.asText()))
                .findAny()
                .map(scheme -> new ContentType(DataType.valueOf(scheme, formatNode.asText(), specNode == null ? null : specNode.asText())))
                .orElseThrow(() -> BaseException.of(SystemCodeMsg.PARAM_WRONG));
    }
}
