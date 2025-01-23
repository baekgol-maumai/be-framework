package ai.maum.beframework.conf.converter.reading;

import ai.maum.beframework.vo.BaseException;
import ai.maum.beframework.vo.meta.type.ContentType;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;

/**
 * 문자열을 콘텐츠 유형으로 변환하는 역직렬화 컨버터
 * @author baekgol@maum.ai
 */
@ReadingConverter
public class ContentTypeReadingConverter implements Converter<String, ContentType> {
    private final ObjectMapper objectMapper;

    public ContentTypeReadingConverter(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public ContentType convert(String source) {
        try {
            return objectMapper.readValue(source, ContentType.class);
        } catch(JsonProcessingException e) {
            throw BaseException.of(e);
        }
    }
}
