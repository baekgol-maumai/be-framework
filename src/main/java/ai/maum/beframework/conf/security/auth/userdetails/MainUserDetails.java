package ai.maum.beframework.conf.security.auth.userdetails;

import lombok.Getter;
import lombok.experimental.SuperBuilder;
import org.bson.types.ObjectId;

/**
 * 주요 사용자 인증 정보
 * @author baekgol@maum.ai
 */
@Getter
@SuperBuilder
public class MainUserDetails extends BaseUserDetails {
    private ObjectId userId;
}
