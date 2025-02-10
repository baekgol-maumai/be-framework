package ai.maum.beframework.conf.security.auth.manager;

import ai.maum.beframework.codemessage.UserCodeMsg;
import ai.maum.beframework.conf.exception.BaseAuthenticationException;
import ai.maum.beframework.conf.security.auth.userdetails.MainUserDetailsService;
import ai.maum.beframework.conf.security.encoder.MainPasswordEncoder;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import reactor.core.publisher.Mono;

/**
 * 주요 사용자 인증 매니저
 * @author baekgol@maum.ai
 */
public class MainUserDetailsAuthenticationManager extends BaseUserDetailsAuthenticationManager {
    public MainUserDetailsAuthenticationManager(MainPasswordEncoder passwordEncoder, MainUserDetailsService userDetailsService) {
        super(passwordEncoder, userDetailsService);
    }

    @Override
    public Mono<Authentication> authenticate(Authentication authentication) {
        return retrieveUser(authentication.getName())
                .filter(ud -> passwordEncoder.matches(authentication.getCredentials().toString(), ud.getPassword()))
                .switchIfEmpty(Mono.error(new BaseAuthenticationException(UserCodeMsg.NOT_EXIST.getMessage())))
                .map(ud -> new UsernamePasswordAuthenticationToken(ud, ud.getPassword(), ud.getAuthorities()));
    }
}
