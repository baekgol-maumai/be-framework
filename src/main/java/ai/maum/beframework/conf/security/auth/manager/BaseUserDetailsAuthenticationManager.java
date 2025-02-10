package ai.maum.beframework.conf.security.auth.manager;

import org.springframework.security.authentication.UserDetailsRepositoryReactiveAuthenticationManager;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * 기본 사용자 인증 매니저
 * @author baekgol@maum.ai
 */
public class BaseUserDetailsAuthenticationManager extends UserDetailsRepositoryReactiveAuthenticationManager {
    protected final PasswordEncoder passwordEncoder;

    public BaseUserDetailsAuthenticationManager(PasswordEncoder passwordEncoder, ReactiveUserDetailsService userDetailsService) {
        super(userDetailsService);
        this.passwordEncoder = passwordEncoder;
    }
}
