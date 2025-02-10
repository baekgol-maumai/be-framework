package ai.maum.beframework.vo.meta.task.engine.llm;

import ai.maum.beframework.codemessage.SystemCodeMsg;
import ai.maum.beframework.vo.BaseException;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

/**
 * LLM 역할
 * @author baekgol@maum.ai
 */
@RequiredArgsConstructor
public enum LlmRole {
    ROLE_SYSTEM("system"),
    ROLE_ASSISTANT("assistant"),
    ROLE_USER("user");

    @JsonValue
    private final String code;

    public static LlmRole of(String role) {
        return Arrays.stream(LlmRole.values())
                .filter(e -> e.code.equals(role))
                .findAny()
                .orElseThrow(() -> BaseException.of(SystemCodeMsg.PARAM_WRONG));
    }

    public static boolean isUser(String role) {
        return role.equals(ROLE_USER.name());
    }
}
