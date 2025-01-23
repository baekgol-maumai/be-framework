package ai.maum.beframework.vo.meta.task.chat;

import ai.maum.beframework.codemessage.SystemCodeMsg;
import ai.maum.beframework.vo.BaseException;
import ai.maum.beframework.vo.meta.Type;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

/**
 * 대화 유형
 * @author baekgol@maum.ai
 */
@Getter
@RequiredArgsConstructor
public enum ChatType implements Type {
    GENERAL("일반", true),
    NOTICE("공지", true);

    private final String name;
    private final boolean isUsed;

    public static ChatType from(String value) {
        return Arrays.stream(ChatType.values())
                .filter(v -> v.name().equals(value))
                .findAny()
                .orElseThrow(() -> BaseException.of(SystemCodeMsg.PARAM_WRONG));
    }
}
