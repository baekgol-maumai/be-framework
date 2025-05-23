package ai.maum.beframework.conf.properties;

import ai.maum.beframework.vo.meta.Properties;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 보안 속성
 * @author baekgol@maum.ai
 */
@Component
@Getter
@Setter
@ConfigurationProperties(prefix = "service.security")
public class SecurityProperties implements Properties {
    public static final long TOKEN_EXPIRE_TIME = 1000L * 60 * 60 * 24;
    public static final String TOKEN_SECRET_KEY = "MindsLABMAUMAccountManagementSystemAMS1234567890";
    public static final String EXCLUDED_PATH_ATTRIBUTE = "is_excluded_path";
    public static final String ROLE_MAUM = "MAUM";
    public static final String ROLE_BASIC = "BASIC";
    public static final String ANONYMOUS_USER_NAME = "ANONYMOUS";
    public static final String ANONYMOUS_USER_PASSWORD = "ANONYMOUS";

    private List<String> allowedOrigins;
    private List<String> allowedHeaders;
    private List<String> allowedMethods;
    private String[] excludedPaths;
}
