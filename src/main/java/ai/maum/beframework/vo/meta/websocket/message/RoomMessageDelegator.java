package ai.maum.beframework.vo.meta.websocket.message;

import ai.maum.beframework.vo.meta.Content;
import ai.maum.beframework.vo.meta.Message;
import ai.maum.beframework.vo.meta.websocket.ResponseEvent;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 방 메시지 전달자
 * @author baekgol@maum.ai
 */
public record RoomMessageDelegator<T>(
        @JsonProperty("res_event")
        ResponseEvent resEvent,
        RoomMessageDelegatorInfo info,
        Content<T> content
) implements Message {
}
