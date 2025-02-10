package ai.maum.beframework.conf.security.encoder;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * API 문서 사용자 비밀번호 인코더
 * @author baekgol@maum.ai
 */
public class DocumentPasswordEncoder extends BCryptPasswordEncoder {
    public DocumentPasswordEncoder() {}
}
