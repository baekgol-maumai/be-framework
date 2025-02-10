package ai.maum.beframework.util;

import ai.maum.beframework.codemessage.SystemCodeMsg;
import ai.maum.beframework.conf.security.auth.userdetails.BaseUserDetails;
import ai.maum.beframework.vo.BaseException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import reactor.core.publisher.Mono;

/**
 * 보안 유틸
 * @author baekgol@maum.ai
 */
public class SecurityUtil {
    private SecurityUtil() {}

    public static Mono<BaseUserDetails> getAuthentication() {
        return ReactiveSecurityContextHolder.getContext()
                .map(SecurityContext::getAuthentication)
                .map(Authentication::getPrincipal)
                .cast(BaseUserDetails.class)
                .switchIfEmpty(Mono.error(BaseException.of(SystemCodeMsg.ACCESS_DENIED)));
    }
}
