package ai.maum.beframework.conf.security.auth.userdetails;

import ai.maum.beframework.conf.properties.SecurityProperties;
import org.springframework.security.core.userdetails.UserDetails;
import reactor.core.publisher.Mono;

/**
 * 익명 사용자 인증 서비스
 * @author baekgol@maum.ai
 */
public class AnonymousUserDetailsService extends BaseUserDetailsService {
    public AnonymousUserDetailsService() {
        super();
    }

    @Override
    public Mono<UserDetails> findByUsername(String username) {
        return Mono.just(AnonymousUserDetails.builder()
                .username(username)
                .password(SecurityProperties.ANONYMOUS_USER_PASSWORD)
                .build());
    }
}
