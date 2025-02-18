package ai.maum.beframework.vo.meta.task.engine.llm;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * LLM 프롬프트
 * @author baekgol@maum.ai
 */
@Getter
@SuperBuilder
@NoArgsConstructor
public class LlmPrompt {
    private LlmRole role;
    private String content;
}
