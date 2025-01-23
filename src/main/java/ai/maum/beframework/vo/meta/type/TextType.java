package ai.maum.beframework.vo.meta.type;

import com.fasterxml.jackson.annotation.JsonTypeName;

/**
 * 글자 유형
 * @author baekgol@maum.ai
 */
@JsonTypeName("TEXT")
public enum TextType implements DataType {
    PLAIN,
    JSON;

    @Override
    public String getScheme() {
        return "TEXT";
    }

    @Override
    public String getFormat() {
        return this.name();
    }
}
