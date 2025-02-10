package ai.maum.beframework.codemessage;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 사용자 코드 메시지
 * @author baekgol@maum.ai
 */
@Getter
@RequiredArgsConstructor
public enum UserCodeMsg implements CodeMessage {
    NOT_EXIST("U000", "사용자 정보가 존재하지 않습니다.");

    private final String code;
    private final String message;
}
