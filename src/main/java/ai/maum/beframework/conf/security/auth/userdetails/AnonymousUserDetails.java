package ai.maum.beframework.conf.security.auth.userdetails;

import lombok.Getter;
import lombok.experimental.SuperBuilder;

/**
 * 익명 사용자 인증 정보
 * @author baekgol@maum.ai
 */
@Getter
@SuperBuilder
public class AnonymousUserDetails extends BaseUserDetails {
}
