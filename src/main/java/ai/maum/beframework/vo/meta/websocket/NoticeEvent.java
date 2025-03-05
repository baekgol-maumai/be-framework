package ai.maum.beframework.vo.meta.websocket;

import ai.maum.beframework.vo.meta.Event;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

/**
 * 공지 이벤트
 * @author baekgol@maum.ai
 */
@Getter
@RequiredArgsConstructor
public enum NoticeEvent implements Event {
    RESPONSE("notice", "response", "공지 응답");

    private final String code;
    private final String name;
    private final String desc;

    public static NoticeEvent getEvent(String code) {
        return Arrays.stream(NoticeEvent.values())
                .filter(e -> e.code.equals(code))
                .findAny()
                .orElse(null);
    }
}
