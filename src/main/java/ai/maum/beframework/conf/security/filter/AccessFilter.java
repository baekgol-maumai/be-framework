package ai.maum.beframework.conf.security.filter;

import ai.maum.beframework.codemessage.SystemCodeMsg;
import ai.maum.beframework.conf.properties.DocumentProperties;
import ai.maum.beframework.conf.properties.SecurityProperties;
import ai.maum.beframework.conf.security.auth.manager.AnonymousAuthenticationManager;
import ai.maum.beframework.conf.security.auth.userdetails.AnonymousUserDetails;
import ai.maum.beframework.vo.BaseException;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatcher;
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatchers;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.Set;

/**
 * 접근 검증 필터
 * @author baekgol@maum.ai
 */
public class AccessFilter implements WebFilter {
    private final ServerWebExchangeMatcher excludeMatcher;
    private final ServerWebExchangeMatcher documentMatcher;
    private final ReactiveAuthenticationManager authenticationManager;
    private static final GrantedAuthority basicAuth = new SimpleGrantedAuthority(SecurityProperties.ROLE_BASIC);

    public AccessFilter(String[] excludedPaths, AnonymousAuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
        excludeMatcher = ServerWebExchangeMatchers.pathMatchers(excludedPaths);
        documentMatcher = ServerWebExchangeMatchers.pathMatchers(DocumentProperties.paths);
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        // 검증이 불필요한 경로인지 확인
        return isExcludedPath(exchange)
                .flatMap(isExcludedPath -> {
                    // 불필요할 경우
                    if(isExcludedPath) {
                        if(exchange.getRequest().getPath().value().equals("/")) {
                            final ServerHttpResponse res = exchange.getResponse();
                            res.setStatusCode(HttpStatus.FOUND);
                            res.getHeaders().setLocation(URI.create("/actuator/health"));
                            return res.setComplete();
                        }

                        exchange.getAttributes().put(SecurityProperties.EXCLUDED_PATH_ATTRIBUTE, true);

                        return chain.filter(exchange);
                    }

                    // 필요할 경우
                    final Set<GrantedAuthority> roles = Set.of(basicAuth);

                    return authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(AnonymousUserDetails.builder()
                            .username(SecurityProperties.ANONYMOUS_USER_NAME)
                            .password(SecurityProperties.ANONYMOUS_USER_PASSWORD)
                            .authorities(roles)
                            .build(),
                            SecurityProperties.ANONYMOUS_USER_PASSWORD,
                            roles))
                            .switchIfEmpty(Mono.error(BaseException.of(SystemCodeMsg.UNAUTHORIZED)))
                            .flatMap(auth -> chain.filter(exchange).contextWrite(ReactiveSecurityContextHolder.withAuthentication(auth)));
                });
    }

    private Mono<Boolean> isExcludedPath(ServerWebExchange exchange) {
        return excludeMatcher.matches(exchange)
                .flatMap(mr -> {
                    if(mr.isMatch()) return Mono.just(true);
                    return documentMatcher.matches(exchange).map(ServerWebExchangeMatcher.MatchResult::isMatch);
                });
    }
}
