package ai.maum.beframework.vo;

import lombok.Data;
import lombok.experimental.SuperBuilder;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.lang.Nullable;

import java.util.Map;

/**
 * REST 요청 양식
 * @author baekgol@maum.ai
 */
@Data
@SuperBuilder
public class RequestInfo<T> {
    private String url;
    private HttpMethod method;
    @Nullable
    private MediaType contentType;
    @Nullable
    private T body;
    @Nullable
    private Map<String, String> headerParams;
    @Nullable
    private Map<String, Object> queryParams;
    @Nullable
    private Integer maxDataSize;
}
