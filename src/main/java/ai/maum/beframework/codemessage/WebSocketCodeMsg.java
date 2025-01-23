package ai.maum.beframework.codemessage;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 웹 소켓 코드 메시지
 * @author baekgol@maum.ai
 */
@Getter
@RequiredArgsConstructor
public enum WebSocketCodeMsg implements CodeMessage {
    ROOM_ID_NOT_EXIST("WS000", "존재하지 않는 방 ID 입니다."),
    USER_ID_NOT_EXIST("WS001", "존재하지 않는 사용자 ID 입니다."),
    ID_IS_EMPTY("WS002", "ID를 빈 문자로 생성할 수 없습니다."),
    USER_ID_ALREADY_EXIST("WS003", "이미 존재하는 사용자 ID 입니다."),
    PACKET_WRONG("WS004", "메시지가 잘못되어 전송할 수 없습니다."),
    CLIENT_ALREADY_CONNECTED("WS005", "이미 연결된 클라이언트가 존재하여 연결할 수 없습니다."),
    NOT_SET_NOTIFY_CONFIG("WS006", "알림 설정이 되어 있지 않습니다."),
    EVENT_WRONG("WS007", "존재하지 않는 이벤트입니다."),
    MESSAGE_FAILURE_ROOM_NOT_EXIST("WS008", "해당 방이 존재하지 않아 메시지를 전송할 수 없습니다."),
    MESSAGE_FAILURE_ROOM_IS_EMPTY("WS009", "해당 방에 사용자가 존재하지 않아 메시지를 전송할 수 없습니다."),
    INVALID_TOKEN("WS010", "올바르지 않거나 만료된 토큰입니다."),
    INVALID_ROOM_NAME("WS011", "사용할 수 없는 방 이름입니다.");

    private final String code;
    private final String message;
}
