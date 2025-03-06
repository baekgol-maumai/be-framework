package ai.maum.beframework.vo.meta.task.engine.llm;

import ai.maum.beframework.codemessage.SystemCodeMsg;
import ai.maum.beframework.vo.BaseException;
import ai.maum.beframework.vo.meta.Type;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

/**
 * LLM 요청 대상
 * @author baekgol@maum.ai
 */
@Getter
@RequiredArgsConstructor
public enum LlmTarget implements Type {
    ORCHESTRA("오케스트라"),
    LANGCHAIN("LangChain");

    private final String name;

    public static LlmTarget from(String value) {
        return Arrays.stream(LlmTarget.values())
                .filter(v -> v.name().equals(value))
                .findAny()
                .orElseThrow(() -> BaseException.of(SystemCodeMsg.PARAM_WRONG));
    }
}
