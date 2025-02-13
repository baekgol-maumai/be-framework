package ai.maum.beframework.conf.security.auth.userdetails;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

/**
 * 기본 사용자 인증 정보
 * @author baekgol@maum.ai
 */
@Getter
@SuperBuilder
public class BaseUserDetails implements UserDetails {
    protected String username;
    protected String password;
    @Setter
    protected Collection<? extends GrantedAuthority> authorities;
}
