package ai.maum.beframework.util;

import ai.maum.beframework.vo.RequestInfo;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * 네트워크 유틸
 * @author baekgol@maum.ai
 */
public class NetworkUtil {
    private static final ObjectMapper om = new ObjectMapper();

    private NetworkUtil() {}

    /**
     * 다른 서버와 HTTP 통신을 수행한다.
     * 단일 응답을 받을 경우 사용한다.
     * @param info 요청 정보
     * @param clazz 응답 클래스
     * @return Mono
     */
    public static <T> Mono<T> request(RequestInfo<?> info, Class<T> clazz) {
        return request(info).bodyToMono(clazz);
    }

    /**
     * 다른 서버와 HTTP 통신을 수행한다.
     * 복수 또는 스트리밍 응답을 받을 경우 사용한다.
     * @param info 요청 정보
     * @param clazz 응답 클래스
     * @return Flux
     */
    public static <T> Flux<T> requestMany(RequestInfo<?> info, Class<T> clazz) {
        return request(info).bodyToFlux(clazz);
    }

    private static WebClient.ResponseSpec request(RequestInfo<?> info) {
        final UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromUriString(info.getUrl());

        if(info.getQueryParams() != null)
            for(String key: info.getQueryParams().keySet()) uriBuilder.queryParam(key, info.getQueryParams().get(key));

        WebClient.Builder builder = WebClient.builder();

        if(info.getMaxDataSize() != null)
            builder = builder.codecs(configurer -> configurer.defaultCodecs().maxInMemorySize(info.getMaxDataSize()));

        final WebClient.RequestBodySpec bodySpec = builder.build()
                .method(info.getMethod())
                .uri(uriBuilder.toUriString())
                .contentType(MediaType.APPLICATION_JSON);

        WebClient.RequestHeadersSpec<?> headersSpec = bodySpec;

        if(info.getBody() != null) headersSpec = bodySpec.bodyValue(info.getBody());
        if(info.getHeaderParams() != null) info.getHeaderParams().forEach(headersSpec::header);

        return headersSpec.retrieve();
    }
}
