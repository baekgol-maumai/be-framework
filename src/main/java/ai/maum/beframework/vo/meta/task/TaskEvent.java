package ai.maum.beframework.vo.meta.task;

import ai.maum.beframework.vo.meta.Event;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

/**
 * 작업 이벤트
 * @author baekgol@maum.ai
 */
@Getter
@RequiredArgsConstructor
public enum TaskEvent implements Event {
    REQUEST("req", "request", "작업 요청"),
    RESPONSE("task", "response", "작업 결과 응답");

    private final String code;
    private final String name;
    private final String desc;

    public static TaskEvent getEvent(String code) {
        return Arrays.stream(TaskEvent.values())
                .filter(e -> e.code.equals(code))
                .findAny()
                .orElse(null);
    }
}
