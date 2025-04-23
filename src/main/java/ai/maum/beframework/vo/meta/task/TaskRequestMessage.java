package ai.maum.beframework.vo.meta.task;

import ai.maum.beframework.vo.meta.Message;
import ai.maum.beframework.vo.meta.task.agent.AgentTool;
import ai.maum.beframework.vo.meta.task.chat.ChatType;
import ai.maum.beframework.vo.meta.task.chatbot.ChatbotType;
import ai.maum.beframework.vo.meta.task.engine.EngineType;
import ai.maum.beframework.vo.meta.task.engine.llm.LlmMultiTurnPrompt;
import ai.maum.beframework.vo.meta.task.engine.llm.LlmPrompt;
import ai.maum.beframework.vo.meta.task.vad.VadType;
import com.fasterxml.jackson.annotation.JsonAlias;
import io.swagger.v3.oas.annotations.media.Schema;
import org.bson.types.ObjectId;

import java.util.List;

/**
 * 작업 요청 메시지
 * @author baekgol@maum.ai
 * @version 1.0.5
 */
public record TaskRequestMessage(Input<?> input,
                                 List<TaskInfo> tasks,
                                 boolean sendLastOnly) implements Message {
    public interface Input<T> {
        T value();
    }

    public record TaskInfo(
            @Schema(name = "trace_id", title = "추적 ID", example = "f2b57164-eeae-430a-94e7-251b287046d5")
            String traceId,
            @Schema(title = "작업 유형", example = "ENGINE")
            TaskType type,
            @Schema(title = "작업 파라미터")
            Param param
    ) {
        public interface Param {
            interface Config {}
            Config config();
        }

        public record EngineParam(
                EngineType type,
                String model,
                String conversationId,
                List<LlmMultiTurnPrompt> prompts,
                EngineConfig config
        ) implements Param {
            public interface EngineInput<T> extends Input<T> {}
            public interface EngineConfig extends Config {}

            public record LlmEngineInput(String value) implements EngineParam.EngineInput<String> {
            }

            public record TtsEngineInput(String value) implements EngineParam.EngineInput<String> {
            }

            public record SttEngineInput(String value) implements EngineParam.EngineInput<String> {
            }

            public record StfEngineInput(String value) implements EngineParam.EngineInput<String> {
            }

            public record LlmEngineConfig(
                    Float topP,
                    Float temperature,
                    Float presencePenalty,
                    Float frequencyPenalty,
                    Integer beamWidth
            ) implements EngineConfig {
            }

            public record TtsEngineConfig(
                    Integer lang,
                    @JsonAlias("sampleRate")
                    Integer sampleRate,
                    Integer speaker,
                    @JsonAlias("audioEncoding")
                    Integer audioEncoding,
                    @JsonAlias("durationRate")
                    Integer durationRate,
                    Integer emotion,
                    Padding padding,
                    String profile,
                    @JsonAlias("speakerName")
                    String speakerName
            ) implements EngineConfig {
                public record Padding(Float begin, Float end) {
                }
            }

            public record SttEngineConfig(
                    String lang
            ) implements EngineConfig {
            }

            public record StfEngineConfig() implements EngineConfig {
            }
        }

        public record ChatParam(
                ChatType type,
                ChatConfig config
        ) implements Param {
            public interface ChatInput<T> extends Input<T> {}
            public interface ChatConfig extends Config {}

            public record GeneralChatInput(String value) implements ChatInput<String> {
            }

            public record NoticeChatInput(String value) implements ChatInput<String> {
            }

            public record GeneralChatConfig() implements ChatConfig {
            }

            public record NoticeChatConfig() implements ChatConfig {
            }
        }

        public record ChatbotParam(
                ChatbotType type,
                String host,
                ChatbotConfig config
        ) implements Param {
            public interface ChatbotInput<T> extends Input<T> {}
            public interface ChatbotConfig extends Config {}

            public record FastAiChatbotInput(String value) implements ChatbotInput<String> {
            }

            public record ScenarioChatbotInput(String value) implements ChatbotInput<String> {
            }

            public record FastAiChatbotConfig() implements ChatbotConfig {
            }

            public record ScenarioChatbotConfig(
                    String lang
            ) implements ChatbotConfig {
            }
        }

        public record RagParam(
                ObjectId id,
                List<LlmPrompt> prompts,
                RagConfig config
        ) implements Param {
            public interface RagInput<T> extends Input<T> {}
            public interface RagConfig extends Config {}

            public record CommonRagInput(String value) implements RagInput<String> {
            }

            public record CommonRagConfig(
                    Integer topK,
                    Float topP,
                    Float temperature,
                    Float presencePenalty,
                    Float frequencyPenalty,
                    Integer beamWidth
            ) implements RagConfig {
            }
        }

        public record VadParam(
                VadType type,
                String target,
                VadConfig config
        ) implements Param {
            public interface VadInput<T> extends Input<T> {
            }

            public interface VadConfig extends Config {
            }

            public record SttVadInput(String value) implements VadInput<String> {
            }

            public record SttVadConfig(
                    Threshold threshold,
                    Integer minSpeechDuration,
                    Integer speechPad,
                    String lang
            ) implements VadConfig {
                public record Threshold(
                        Float start,
                        Float end
                ) {
                }
            }
        }

        public record AgentParam(
                String model,
                String conversationId,
                List<AgentTool> tools,
                AgentConfig config
        ) implements Param {
            public interface AgentInput<T> extends Input<T> {}
            public interface AgentConfig extends Config {}

            public record CommonAgentInput(String value) implements AgentInput<String> {
            }

            public record CommonAgentConfig(
                    Float topP,
                    Float temperature,
                    Float presencePenalty,
                    Float frequencyPenalty,
                    Integer beamWidth
            ) implements AgentConfig {
            }
        }
    }
}
