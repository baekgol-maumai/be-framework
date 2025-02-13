package ai.maum.beframework.conf.security.encoder;

import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * 익명 사용자 비밀번호 인코더
 * @author baekgol@maum.ai
 */
public class AnonymousPasswordEncoder implements PasswordEncoder {
    public AnonymousPasswordEncoder() {}

    @Override
    public String encode(CharSequence rawPassword) {
        return null;
    }

    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        return rawPassword.equals(encodedPassword);
    }
}
