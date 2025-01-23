package ai.maum.beframework.vo.meta.type;

import ai.maum.beframework.vo.meta.Type;
import ai.maum.beframework.vo.meta.type.binary.BinaryType;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import org.springframework.lang.Nullable;

/**
 * 데이터 유형 인터페이스
 * @author baekgol@maum.ai
 */
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "scheme")
@JsonSubTypes({
        @JsonSubTypes.Type(value = TextType.class, name = "TEXT"),
        @JsonSubTypes.Type(value = BinaryType.class, name = "BINARY")
})
public interface DataType extends Type {
    @JsonIgnore
    String getScheme();
    @JsonProperty
    String getFormat();

    static DataType valueOf(ContentType.Scheme scheme, String format, @Nullable String spec) {
        return switch(scheme) {
            case TEXT -> TextType.valueOf(format);
            case BINARY -> BinaryType.valueOf(format, spec); };
    }
}
