package ai.maum.beframework.conf.security.auth.userdetails;

import org.springframework.security.core.userdetails.ReactiveUserDetailsService;

/**
 * 기본 사용자 인증 서비스
 * @author baekgol@maum.ai
 */
public abstract class BaseUserDetailsService implements ReactiveUserDetailsService {
    public BaseUserDetailsService() {}
}
