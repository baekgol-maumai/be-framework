package ai.maum.beframework.vo.meta.task;

import ai.maum.beframework.codemessage.SystemCodeMsg;
import ai.maum.beframework.vo.BaseException;
import ai.maum.beframework.vo.meta.Type;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

/**
 * 작업 유형
 * @author baekgol@maum.ai
 */
@Getter
@RequiredArgsConstructor
public enum TaskType implements Type {
    ENGINE("엔진"),
    CHAT("대화"),
    CHATBOT("챗봇"),
    RAG("RAG"),
    VAD("VAD"),
    AGENT("에이전트");

    private final String name;

    public static TaskType from(String value) {
        return Arrays.stream(TaskType.values())
                .filter(v -> v.name().equals(value))
                .findAny()
                .orElseThrow(() -> BaseException.of(SystemCodeMsg.PARAM_WRONG));
    }
}
