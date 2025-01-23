package ai.maum.beframework.conf.converter.writing;

import ai.maum.beframework.vo.BaseException;
import ai.maum.beframework.vo.meta.type.ContentType;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.WritingConverter;

/**
 * 콘텐츠 유형을 문자열로 변환하는 직렬화 컨버터
 * @author baekgol@maum.ai
 */
@WritingConverter
public class ContentTypeWritingConverter implements Converter<ContentType, String> {
    private final ObjectMapper objectMapper;

    public ContentTypeWritingConverter(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public String convert(ContentType source) {
        try {
            return objectMapper.writeValueAsString(source);
        } catch(JsonProcessingException e) {
            throw BaseException.of(e);
        }
    }
}
