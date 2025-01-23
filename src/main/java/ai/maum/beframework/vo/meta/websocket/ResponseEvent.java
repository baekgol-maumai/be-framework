package ai.maum.beframework.vo.meta.websocket;

import ai.maum.beframework.vo.meta.Event;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 응답 이벤트
 * @author baekgol@maum.ai
 */
@Getter
@RequiredArgsConstructor
public enum ResponseEvent implements Event {
    START("start", "response start", "응답 시작"),
    DATA("data", "response", "응답 중"),
    END("end", "response end", "응답 종료");

    private final String code;
    private final String name;
    private final String desc;
}
