package ai.maum.beframework.conf.security.auth;

import ai.maum.beframework.codemessage.SystemCodeMsg;
import ai.maum.beframework.vo.BaseException;
import ai.maum.beframework.vo.BaseResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.server.ServerAuthenticationEntryPoint;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * 기본 인증 예외 처리 핸들러
 * @author baekgol@maum.ai
 */
public class BaseAuthenticationEntryPoint implements ServerAuthenticationEntryPoint {
    private final ObjectMapper om;

    BaseAuthenticationEntryPoint(ObjectMapper om) {
        this.om = om;
    }

    @Override
    public Mono<Void> commence(ServerWebExchange exchange, AuthenticationException ex) {
        final ServerHttpResponse res = exchange.getResponse();

        res.setStatusCode(HttpStatus.OK);
        res.getHeaders().setContentType(MediaType.APPLICATION_JSON);

        try {
            return res.writeWith(Flux.just(res.bufferFactory()
                    .wrap(om.writeValueAsString(BaseResponse.failure(SystemCodeMsg.UNAUTHORIZED)).getBytes())));
        } catch(JsonProcessingException e) {
            throw BaseException.of(SystemCodeMsg.CONVERT_FAILURE);
        }
    }
}
