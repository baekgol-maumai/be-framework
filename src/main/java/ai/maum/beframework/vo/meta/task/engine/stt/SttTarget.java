package ai.maum.beframework.vo.meta.task.engine.stt;

import ai.maum.beframework.codemessage.SystemCodeMsg;
import ai.maum.beframework.vo.BaseException;
import ai.maum.beframework.vo.meta.Type;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

/**
 * STT 요청 대상
 * @author baekgol@maum.ai
 */
@Getter
@RequiredArgsConstructor
public enum SttTarget implements Type {
    ORCHESTRA("오케스트라"),
    TASK_SUPPORTER("작업 지원자");

    private final String name;

    public static SttTarget from(String value) {
        return Arrays.stream(SttTarget.values())
                .filter(v -> v.name().equals(value))
                .findAny()
                .orElseThrow(() -> BaseException.of(SystemCodeMsg.PARAM_WRONG));
    }
}
