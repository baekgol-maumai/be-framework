package ai.maum.beframework.conf.exception;

import org.springframework.security.core.AuthenticationException;

/**
 * 기본 인증 예외
 * @author baekgol@maum.ai
 */
public class BaseAuthenticationException extends AuthenticationException {
    public BaseAuthenticationException(String msg) {
        super(msg);
    }
}
