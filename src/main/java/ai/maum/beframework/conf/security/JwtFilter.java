package ai.maum.beframework.conf.security;

import ai.maum.beframework.vo.BaseException;
import ai.maum.beframework.codemessage.SystemCodeMsg;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatcher;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.List;

/**
 * JWT 검증 필터
 * @author baekgol@maum.ai
 */
public class JwtFilter implements WebFilter {
    private final ServerWebExchangeMatcher correctPathMatcher;
    private final ReactiveAuthenticationManager authenticationManager;

    JwtFilter(List<String> excludedPaths,
              ReactiveAuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
        correctPathMatcher = new CorrectPathMatcher(excludedPaths.toArray(new String[0]));
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        return correctPathMatcher.matches(exchange)
                .flatMap(mr -> {
                    if(!mr.isMatch()) {
                        if(exchange.getRequest().getPath().value().equals("/")) {
                            final ServerHttpResponse res = exchange.getResponse();
                            res.setStatusCode(HttpStatus.FOUND);
                            res.getHeaders().setLocation(URI.create("/actuator/health"));
                            return res.setComplete();
                        }

                        return chain.filter(exchange);
                    }

                    return Mono.justOrEmpty(exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION))
                            .switchIfEmpty(Mono.error(BaseException.of(SystemCodeMsg.UNAUTHORIZED)))
                            .map(token -> token.replace("Bearer ", ""))
                            .then(chain.filter(exchange));
                });
    }
}
