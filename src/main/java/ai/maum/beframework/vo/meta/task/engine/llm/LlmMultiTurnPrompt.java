package ai.maum.beframework.vo.meta.task.engine.llm;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * LLM 멀티턴 프롬프트
 * @author baekgol@maum.ai
 */
@Getter
@SuperBuilder
@NoArgsConstructor
public class LlmMultiTurnPrompt extends LlmPrompt {
    private int session;
}
