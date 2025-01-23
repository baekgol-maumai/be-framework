package ai.maum.beframework.vo.meta.websocket.message;

import ai.maum.beframework.vo.meta.Content;
import ai.maum.beframework.vo.meta.Message;
import ai.maum.beframework.vo.meta.websocket.ResponseEvent;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 방 메시지
 * @author baekgol@maum.ai
 */
public record RoomMessage<T>(
        @JsonProperty("msg_type")
        MessageType msgType,
        @JsonProperty("res_event")
        ResponseEvent resEvent,
        RoomMessageInfo info,
        Content<T> content
) implements Message {
}
