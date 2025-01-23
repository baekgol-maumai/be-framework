package ai.maum.beframework.conf.security;

import ai.maum.beframework.codemessage.DocumentUserCodeMsg;
import ai.maum.beframework.conf.exception.BaseAuthenticationException;
import org.springframework.security.authentication.UserDetailsRepositoryReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import reactor.core.publisher.Mono;

/**
 * 기본 사용자 인증 매니저
 * @author baekgol@maum.ai
 */
public class BaseUserDetailsAuthenticationManager extends UserDetailsRepositoryReactiveAuthenticationManager {
    private final PasswordEncoder passwordEncoder;

    BaseUserDetailsAuthenticationManager(PasswordEncoder passwordEncoder, ReactiveUserDetailsService userDetailsService) {
        super(userDetailsService);
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Mono<Authentication> authenticate(Authentication authentication) {
        return retrieveUser(authentication.getName())
                .filter(ud -> passwordEncoder.matches(authentication.getCredentials().toString(), ud.getPassword()))
                .switchIfEmpty(Mono.error(new BaseAuthenticationException(DocumentUserCodeMsg.NOT_EXIST.getMessage())))
                .map(ud -> new UsernamePasswordAuthenticationToken(ud, ud.getPassword(), ud.getAuthorities()));
    }
}
