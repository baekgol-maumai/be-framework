package ai.maum.beframework.conf.security.filter;

import ai.maum.beframework.codemessage.SystemCodeMsg;
import ai.maum.beframework.conf.properties.SecurityProperties;
import ai.maum.beframework.conf.security.auth.manager.JwtAuthenticationManager;
import ai.maum.beframework.conf.security.auth.userdetails.AnonymousUserDetails;
import ai.maum.beframework.conf.security.auth.userdetails.JwtUserDetails;
import ai.maum.beframework.model.repository.UserRepository;
import ai.maum.beframework.util.JwtUtil;
import ai.maum.beframework.util.SecurityUtil;
import ai.maum.beframework.vo.BaseException;
import io.jsonwebtoken.Claims;
import org.bson.types.ObjectId;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

/**
 * JWT 검증 필터
 * @author baekgol@maum.ai
 */
public class JwtFilter implements WebFilter {
    private final UserRepository userRepository;
    private final JwtAuthenticationManager authenticationManager;
    private static final GrantedAuthority maumAuth = new SimpleGrantedAuthority(SecurityProperties.ROLE_MAUM);

    public JwtFilter(UserRepository userRepository, JwtAuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.authenticationManager = authenticationManager;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        return Optional.ofNullable(exchange.getAttribute(SecurityProperties.EXCLUDED_PATH_ATTRIBUTE))
                // 검증 불필요
                .map(isExcludedPath -> chain.filter(exchange))
                // 검증 필요
                .orElse(SecurityUtil.getAuthentication()
                        .cast(AnonymousUserDetails.class)
                        .flatMap(ud -> {
                            final String token = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);

                            // 토큰이 존재하지 않을 경우 검증 생략
                            if(token == null)
                                return chain.filter(exchange);

                            final Claims claims = JwtUtil.validateAndReturn(token.replace("Bearer ", ""));

                            return userRepository.findById(new ObjectId(claims.get("userId", String.class)))
                                    .map(user -> {
                                        final Set<GrantedAuthority> roles = new HashSet<>(ud.getAuthorities());
                                        roles.add(maumAuth);

                                        return new UsernamePasswordAuthenticationToken(JwtUserDetails.builder()
                                                .username(user.getEmail())
                                                .password(user.getPassword())
                                                .userId(user.getId())
                                                .authorities(roles)
                                                .build(),
                                                user.getPassword(),
                                                roles);
                                    })
                                    .flatMap(authenticationManager::authenticate)
                                    .switchIfEmpty(Mono.error(BaseException.of(SystemCodeMsg.UNAUTHORIZED)))
                                    .flatMap(auth -> chain.filter(exchange).contextWrite(ReactiveSecurityContextHolder.withAuthentication(auth)));
                        }));
    }
}
