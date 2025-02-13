package ai.maum.beframework.conf.security.auth.userdetails;

import lombok.Getter;
import lombok.experimental.SuperBuilder;
import org.bson.types.ObjectId;

/**
 * JWT 사용자 인증 정보
 * @author baekgol@maum.ai
 */
@Getter
@SuperBuilder
public class JwtUserDetails extends BaseUserDetails {
    private ObjectId userId;
}
