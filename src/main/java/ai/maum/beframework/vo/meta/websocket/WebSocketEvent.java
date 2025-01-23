package ai.maum.beframework.vo.meta.websocket;

import ai.maum.beframework.vo.meta.Event;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

/**
 * 웹 소켓 이벤트
 * @author baekgol@maum.ai
 */
@Getter
@RequiredArgsConstructor
public enum WebSocketEvent implements Event {
    CONNECT("connect", "Connect", "연결 완료"),
    JOIN("join", "Join", "사용자 방 참여"),
    LEAVE("leave", "Leave", "사용자 방 퇴장"),
    STOP("stop", "Stop", "작업 중단 요청"),
    ERROR("error", "Error", "오류 발생"),
    USER_DISCONNECT("user_disconnect", "User Disconnect", "사용자 측 연결 해제");

    private final String code;
    private final String name;
    private final String desc;

    public static WebSocketEvent getEvent(String code) {
        return Arrays.stream(WebSocketEvent.values())
                .filter(e -> e.code.equals(code))
                .findAny()
                .orElse(null);
    }
}
