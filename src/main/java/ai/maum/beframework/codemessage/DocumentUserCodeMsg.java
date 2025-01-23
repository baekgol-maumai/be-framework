package ai.maum.beframework.codemessage;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * API 문서 사용자 코드 메시지
 * @author baekgol@maum.ai
 */
@Getter
@RequiredArgsConstructor
public enum DocumentUserCodeMsg implements CodeMessage {
    NOT_EXIST("DU000", "사용자 정보가 존재하지 않습니다.");

    private final String code;
    private final String message;
}
