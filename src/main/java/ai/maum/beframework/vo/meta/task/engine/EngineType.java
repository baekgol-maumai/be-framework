package ai.maum.beframework.vo.meta.task.engine;

import ai.maum.beframework.codemessage.SystemCodeMsg;
import ai.maum.beframework.vo.BaseException;
import ai.maum.beframework.vo.meta.Type;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

/**
 * 엔진 유형
 * @author baekgol@maum.ai
 */
@Getter
@RequiredArgsConstructor
public enum EngineType implements Type {
    LLM("LLM"),
    TTS("TTS"),
    STT("STT"),
    STF("STF");

    private final String name;

    public static EngineType from(String value) {
        return Arrays.stream(EngineType.values())
                .filter(v -> v.name().equals(value))
                .findAny()
                .orElseThrow(() -> BaseException.of(SystemCodeMsg.PARAM_WRONG));
    }
}
