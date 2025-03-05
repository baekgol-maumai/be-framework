package ai.maum.beframework.vo.meta.task.vad;

import ai.maum.beframework.codemessage.SystemCodeMsg;
import ai.maum.beframework.vo.BaseException;
import ai.maum.beframework.vo.meta.Type;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

/**
 * VAD 유형
 * @author baekgol@maum.ai
 */
@Getter
@RequiredArgsConstructor
public enum VadType implements Type {
    STT("STT");

    private final String name;

    public static VadType from(String value) {
        return Arrays.stream(VadType.values())
                .filter(v -> v.name().equals(value))
                .findAny()
                .orElseThrow(() -> BaseException.of(SystemCodeMsg.PARAM_WRONG));
    }
}
