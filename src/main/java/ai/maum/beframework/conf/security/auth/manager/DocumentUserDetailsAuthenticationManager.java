package ai.maum.beframework.conf.security.auth.manager;

import ai.maum.beframework.codemessage.DocumentUserCodeMsg;
import ai.maum.beframework.conf.exception.BaseAuthenticationException;
import ai.maum.beframework.conf.security.auth.userdetails.DocumentUserDetailsService;
import ai.maum.beframework.conf.security.encoder.DocumentPasswordEncoder;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import reactor.core.publisher.Mono;

/**
 * API 문서 사용자 인증 매니저
 * @author baekgol@maum.ai
 */
public class DocumentUserDetailsAuthenticationManager extends BaseUserDetailsAuthenticationManager {
    public DocumentUserDetailsAuthenticationManager(DocumentPasswordEncoder passwordEncoder, DocumentUserDetailsService userDetailsService) {
        super(passwordEncoder, userDetailsService);
    }

    @Override
    public Mono<Authentication> authenticate(Authentication authentication) {
        return retrieveUser(authentication.getName())
                .filter(ud -> passwordEncoder.matches(authentication.getCredentials().toString(), ud.getPassword()))
                .switchIfEmpty(Mono.error(new BaseAuthenticationException(DocumentUserCodeMsg.NOT_EXIST.getMessage())))
                .map(ud -> new UsernamePasswordAuthenticationToken(ud, ud.getPassword(), ud.getAuthorities()));
    }
}
