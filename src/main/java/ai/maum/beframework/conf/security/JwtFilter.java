package ai.maum.beframework.conf.security;

import ai.maum.beframework.codemessage.SystemCodeMsg;
import ai.maum.beframework.conf.security.auth.userdetails.MainUserDetails;
import ai.maum.beframework.model.repository.UserRepository;
import ai.maum.beframework.util.JwtUtil;
import ai.maum.beframework.vo.BaseException;
import org.bson.types.ObjectId;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
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
    private final UserRepository userRepository;
    private final ServerWebExchangeMatcher correctPathMatcher;
    private final ReactiveAuthenticationManager authenticationManager;

    JwtFilter(List<String> excludedPaths,
              UserRepository userRepository,
              ReactiveAuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
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
                            .map(token -> JwtUtil.validateAndReturn(token.replace("Bearer ", "")))
                            .flatMap(claims -> userRepository.findById(new ObjectId(claims.get("userId", String.class)))
                                    .map(user -> new UsernamePasswordAuthenticationToken(MainUserDetails.builder()
                                            .username(user.getEmail())
                                            .password(user.getPassword())
                                            .userId(user.getId())
                                            .build(),
                                            user.getPassword(),
                                            null)))
                            .flatMap(authenticationManager::authenticate)
                            .switchIfEmpty(Mono.error(BaseException.of(SystemCodeMsg.UNAUTHORIZED)))
                            .flatMap(auth -> chain.filter(exchange).contextWrite(ReactiveSecurityContextHolder.withAuthentication(auth)));
                });
    }
}
