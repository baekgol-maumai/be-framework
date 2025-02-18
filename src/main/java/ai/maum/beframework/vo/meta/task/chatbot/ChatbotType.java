package ai.maum.beframework.vo.meta.task.chatbot;

import ai.maum.beframework.codemessage.SystemCodeMsg;
import ai.maum.beframework.vo.BaseException;
import ai.maum.beframework.vo.meta.Type;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

/**
 * 챗봇 유형
 * @author baekgol@maum.ai
 */
@Getter
@RequiredArgsConstructor
public enum ChatbotType implements Type {
    FAST_AI("FAST AI"),
    SCENARIO("시나리오");

    private final String name;

    public static ChatbotType from(String value) {
        return Arrays.stream(ChatbotType.values())
                .filter(v -> v.name().equals(value))
                .findAny()
                .orElseThrow(() -> BaseException.of(SystemCodeMsg.PARAM_WRONG));
    }
}
