package ai.maum.beframework.vo.meta.task;

import ai.maum.beframework.util.StringUtil;
import ai.maum.beframework.vo.meta.Message;
import ai.maum.beframework.vo.meta.task.chat.ChatType;
import ai.maum.beframework.vo.meta.task.chatbot.ChatbotType;
import ai.maum.beframework.vo.meta.task.engine.EngineType;
import ai.maum.beframework.vo.meta.task.engine.llm.LlmMultiTurnPrompt;
import ai.maum.beframework.vo.meta.task.engine.llm.LlmPrompt;
import com.fasterxml.jackson.annotation.JsonAlias;
import org.bson.types.ObjectId;

import java.util.List;

/**
 * 작업 요청 메시지
 * @author baekgol@maum.ai
 * @version 1.0.2
 */
public record TaskRequestMessage(Input<?> input,
                                 List<TaskInfo> tasks,
                                 boolean sendLastOnly) implements Message {
    public interface Input<T> {
        T value();
    }

    public record TaskInfo(
            String traceId,
            TaskType type,
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

            public record LlmEngineInput(String value) implements EngineInput<String> {
                @Override
                public String toString() {
                    return value;
                }
            }

            public record TtsEngineInput(String value) implements EngineInput<String> {
                @Override
                public String toString() {
                    return StringUtil.abbreviate(value);
                }
            }

            public record SttEngineInput(String value) implements EngineInput<String> {
                @Override
                public String toString() {
                    return StringUtil.abbreviate(value);
                }
            }

            public record StfEngineInput(String value) implements EngineInput<String> {
                @Override
                public String toString() {
                    return StringUtil.abbreviate(value);
                }
            }

            public record LlmEngineConfig(
                    Float topP,
                    Float temperature,
                    Float presencePenalty,
                    Float frequencyPenalty,
                    Integer beamWidth
            ) implements EngineConfig {
                @Override
                public String toString() {
                    String result = "";
                    boolean isFirst = true;

                    if(topP != null) {
                        result += ("Top P: " + topP);
                        isFirst = false;
                    }

                    if(temperature != null) {
                        if(!isFirst) result += ", ";
                        result += ("Temperature: " + temperature);
                        isFirst = false;
                    }

                    if(presencePenalty != null) {
                        if(!isFirst) result += ", ";
                        result += ("Presence Penalty: " + presencePenalty);
                        isFirst = false;
                    }

                    if(frequencyPenalty != null) {
                        if(!isFirst) result += ", ";
                        result += ("Frequency Penalty: " + frequencyPenalty);
                        isFirst = false;
                    }

                    if(beamWidth != null) {
                        if(!isFirst) result += ", ";
                        result += ("Beam Width: " + beamWidth);
                    }

                    return result;
                }
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
                @Override
                public String toString() {
                    String result = "";
                    boolean isFirst = true;

                    if(lang != null) {
                        result += ("Lang: " + lang);
                        isFirst = false;
                    }

                    if(sampleRate != null) {
                        if(!isFirst) result += ", ";
                        result += ("Sample Rate: " + sampleRate);
                        isFirst = false;
                    }

                    if(speaker != null) {
                        if(!isFirst) result += ", ";
                        result += ("Speaker: " + speaker);
                        isFirst = false;
                    }

                    if(audioEncoding != null) {
                        if(!isFirst) result += ", ";
                        result += ("Audio Encoding: " + audioEncoding);
                        isFirst = false;
                    }

                    if(durationRate != null) {
                        if(!isFirst) result += ", ";
                        result += ("Duration Rate: " + durationRate);
                        isFirst = false;
                    }

                    if(emotion != null) {
                        if(!isFirst) result += ", ";
                        result += ("Emotion: " + emotion);
                        isFirst = false;
                    }

                    if(padding != null) {
                        if(!isFirst) result += ", ";
                        result += ("Padding: " + padding);
                        isFirst = false;
                    }

                    if(profile != null) {
                        if(!isFirst) result += ", ";
                        result += ("Profile: " + profile);
                        isFirst = false;
                    }

                    if(speakerName != null) {
                        if(!isFirst) result += ", ";
                        result += ("Speaker Name: " + speakerName);
                    }

                    return result;
                }

                public record Padding(Float begin, Float end) {
                    @Override
                    public String toString() {
                        return "("
                                + (begin == null ? "null" : begin)
                                + ", "
                                + (end == null ? "null" : end)
                                + ")";
                    }
                }
            }

            public record SttEngineConfig() implements EngineConfig {
                @Override
                public String toString() {
                    return "없음";
                }
            }

            public record StfEngineConfig() implements EngineConfig {
                @Override
                public String toString() {
                    return "없음";
                }
            }

            @Override
            public String toString() {
                return "엔진 유형: "
                        + type.getName()
                        + ", 엔진 모델: "
                        + model
                        + ", 설정: "
                        + config;
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
                @Override
                public String toString() {
                    return "없음";
                }
            }

            public record NoticeChatConfig() implements ChatConfig {
                @Override
                public String toString() {
                    return "없음";
                }
            }

            @Override
            public String toString() {
                return "대화 유형: "
                        + type.getName()
                        + ", 설정: "
                        + config;
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
                @Override
                public String toString() {
                    return "없음";
                }
            }

            public record ScenarioChatbotConfig(
                    String lang
            ) implements ChatbotConfig {
                @Override
                public String toString() {
                    return "언어: " + lang;
                }
            }

            @Override
            public String toString() {
                return "유형: "
                        + type
                        + ", 호스트: "
                        + host
                        + ", 설정: "
                        + config;
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
                @Override
                public String toString() {
                    String result = "";
                    boolean isFirst = true;

                    if(topK != null) {
                        result += ("Top K: " + topK);
                        isFirst = false;
                    }

                    if(topP != null) {
                        if(!isFirst) result += ", ";
                        result += ("Top P: " + topP);
                        isFirst = false;
                    }

                    if(temperature != null) {
                        if(!isFirst) result += ", ";
                        result += ("Temperature: " + temperature);
                        isFirst = false;
                    }

                    if(presencePenalty != null) {
                        if(!isFirst) result += ", ";
                        result += ("Presence Penalty: " + presencePenalty);
                        isFirst = false;
                    }

                    if(frequencyPenalty != null) {
                        if(!isFirst) result += ", ";
                        result += ("Frequency Penalty: " + frequencyPenalty);
                        isFirst = false;
                    }

                    if(beamWidth != null) {
                        if(!isFirst) result += ", ";
                        result += ("Beam Width: " + beamWidth);
                    }

                    return result;
                }
            }

            @Override
            public String toString() {
                return "설정: " + config;
            }
        }

        @Override
        public String toString() {
            return "\n[작업 유형]\n"
                    + type.getName()
                    + "[파라미터]\n"
                    + param;
        }
    }

    @Override
    public String toString() {
        final int taskSize = tasks.size();
        final StringBuilder result = new StringBuilder("[입력값]\n"
                + input
                + "\n[작업 정보]\n");

        result.append("호출 순서: (");

        for(int i=0; i<taskSize; i++) {
            result.append(tasks.get(i).type.getName());
            if(i < taskSize - 1) result.append(" -> ");
        }

        result.append(")\n");

        for(int i=0; i<taskSize; i++) {
            result.append("(")
                    .append(i + 1)
                    .append("): ")
                    .append(tasks.get(i));
            if(i < taskSize - 1) result.append("\n");
        }

        return result.toString();
    }
}
