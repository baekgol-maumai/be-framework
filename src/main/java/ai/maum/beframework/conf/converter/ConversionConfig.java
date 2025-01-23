package ai.maum.beframework.conf.converter;

import ai.maum.beframework.conf.converter.reading.ContentTypeReadingConverter;
import ai.maum.beframework.conf.converter.writing.ContentTypeWritingConverter;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.convert.MongoCustomConversions;

import java.util.List;

/**
 * 변환 설정
 * @author baekgol@maum.ai
 */
@Configuration
public class ConversionConfig {
    @Bean
    public MongoCustomConversions mongoCustomConversions(ObjectMapper objectMapper) {
        return new MongoCustomConversions(List.of(
                new ContentTypeReadingConverter(objectMapper),
                new ContentTypeWritingConverter(objectMapper)));
    }
}
