package ai.maum.beframework.codemessage;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 시스템 코드 메시지
 * @author baekgol@maum.ai
 */
@Getter
@RequiredArgsConstructor
public enum SystemCodeMsg implements CodeMessage {
    SUCCESS("SYS000", "성공적으로 수행하였습니다."),
    FAILURE("SYS001", "알 수 없는 오류가 발생했습니다."),
    CONVERT_FAILURE("SYS002", "데이터 변환에 실패하였습니다."),
    PARAM_WRONG("SYS003", "요청 파라미터가 올바르지 않습니다."),
    DATA_SEARCH_FAILURE("SYS004", "데이터 조회에 실패하였습니다."),
    UNAUTHORIZED("SYS005", "인증되지 않은 사용자입니다."),
    ACCESS_DENIED("SYS006", "권한이 존재하지 않습니다.");

    private final String code;
    private final String message;
}
