package ai.maum.beframework.conf.properties;

import ai.maum.beframework.vo.meta.Properties;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * API 문서 속성
 * @author baekgol@maum.ai
 */
@Component
@Getter
@Setter
@ConfigurationProperties(prefix = "service.document")
public class DocumentProperties implements Properties {
    public static final String[] paths = new String[]{ "/doc", "/webjars/**", "/v3/**" };

    private String title;
    private String desc;
    private List<String> serverUrl;
}
