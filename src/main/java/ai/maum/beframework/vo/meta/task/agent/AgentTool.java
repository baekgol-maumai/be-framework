package ai.maum.beframework.vo.meta.task.agent;

import java.util.List;
import java.util.Map;

/**
 * 에이전트 도구
 * @author baekgol@maum.ai
 */
public record AgentTool(
        String executeTarget,
        String name,
        List<String> args,
        Map<String, String> env
) {
}
