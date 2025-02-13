package ai.maum.beframework.conf.security.auth.manager;

import ai.maum.beframework.codemessage.UserCodeMsg;
import ai.maum.beframework.conf.exception.BaseAuthenticationException;
import ai.maum.beframework.conf.security.auth.userdetails.JwtUserDetails;
import ai.maum.beframework.conf.security.auth.userdetails.JwtUserDetailsService;
import ai.maum.beframework.conf.security.encoder.JwtPasswordEncoder;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import reactor.core.publisher.Mono;

/**
 * JWT 사용자 인증 매니저
 * @author baekgol@maum.ai
 */
public class JwtAuthenticationManager extends BaseAuthenticationManager {
    public JwtAuthenticationManager(JwtPasswordEncoder passwordEncoder, JwtUserDetailsService userDetailsService) {
        super(passwordEncoder, userDetailsService);
    }

    @Override
    public Mono<Authentication> authenticate(Authentication authentication) {
        return retrieveUser(authentication.getName())
                .cast(JwtUserDetails.class)
                .filter(ud -> passwordEncoder.matches(authentication.getCredentials().toString(), ud.getPassword()))
                .switchIfEmpty(Mono.error(new BaseAuthenticationException(UserCodeMsg.NOT_EXIST.getMessage())))
                .doOnNext(ud -> ud.setAuthorities(authentication.getAuthorities()))
                .map(ud -> new UsernamePasswordAuthenticationToken(ud, ud.getPassword(), ud.getAuthorities()));
    }
}
