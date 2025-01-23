package ai.maum.beframework.vo.meta;

import ai.maum.beframework.vo.meta.type.ContentType;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * 콘텐츠
 * @author baekgol@maum.ai
 */
@Data
@Builder
@JsonDeserialize(builder = Content.ContentBuilder.class)
public class Content<T> {
    private ContentType type;
    private T data;

    @Data
    @Builder
    public static class Stf {
        private String audio;
        private List<String> images;
    }
}
