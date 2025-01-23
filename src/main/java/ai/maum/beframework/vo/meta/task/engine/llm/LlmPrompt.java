package ai.maum.beframework.vo.meta.task.engine.llm;

/**
 * LLM 프롬프트
 * @author baekgol@maum.ai
 */
public record LlmPrompt(
        LlmRole role,
        String content
) {
}
