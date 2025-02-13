package ai.maum.beframework.conf.security.auth.manager;

import ai.maum.beframework.conf.security.auth.userdetails.AnonymousUserDetails;
import ai.maum.beframework.conf.security.auth.userdetails.AnonymousUserDetailsService;
import ai.maum.beframework.conf.security.encoder.AnonymousPasswordEncoder;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import reactor.core.publisher.Mono;

/**
 * 익명 사용자 인증 매니저
 * @author baekgol@maum.ai
 */
public class AnonymousAuthenticationManager extends BaseAuthenticationManager {
    public AnonymousAuthenticationManager(AnonymousPasswordEncoder passwordEncoder, AnonymousUserDetailsService userDetailsService) {
        super(passwordEncoder, userDetailsService);
    }

    @Override
    public Mono<Authentication> authenticate(Authentication authentication) {
        return retrieveUser(authentication.getName())
                .cast(AnonymousUserDetails.class)
                .doOnNext(ud -> ud.setAuthorities(authentication.getAuthorities()))
                .map(ud -> new UsernamePasswordAuthenticationToken(ud, ud.getPassword(), ud.getAuthorities()));
    }
}
