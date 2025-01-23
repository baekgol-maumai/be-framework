package ai.maum.beframework.vo.meta.websocket.message;

import ai.maum.beframework.vo.meta.Message;

/**
 * 오류 메시지
 * @author baekgol@maum.ai
 */
public record ErrorMessage(
        String code,
        String message
) implements Message {
}
