package ai.maum.beframework.vo.meta.websocket.message;

import ai.maum.beframework.vo.meta.Type;
import ai.maum.beframework.vo.meta.websocket.WebSocketEvent;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 메시지 유형
 * @author baekgol@maum.ai
 */
@Getter
@RequiredArgsConstructor
public enum MessageType implements Type {
    TASK("task"),
    NOTICE("notice"),
    JOIN(WebSocketEvent.JOIN.getCode()),
    LEAVE(WebSocketEvent.LEAVE.getCode());

    private final String code;
}
