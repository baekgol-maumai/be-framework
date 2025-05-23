package ai.maum.beframework.vo.meta.task;

import ai.maum.beframework.codemessage.CodeMessage;
import ai.maum.beframework.conf.serialization.deserializer.CodeMessageDeserializer;
import ai.maum.beframework.conf.serialization.deserializer.ObjectIdDeserializer;
import ai.maum.beframework.conf.serialization.serializer.CodeMessageSerializer;
import ai.maum.beframework.conf.serialization.serializer.ObjectIdSerializer;
import ai.maum.beframework.vo.meta.task.chat.ChatType;
import ai.maum.beframework.vo.meta.task.chatbot.ChatbotType;
import ai.maum.beframework.vo.meta.task.engine.EngineType;
import ai.maum.beframework.vo.meta.task.vad.VadType;
import ai.maum.beframework.vo.meta.websocket.message.RoomMessageDelegatorInfo;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.bson.types.ObjectId;

/**
 * 작업 메시지 전달자 정보
 * @author baekgol@maum.ai
 * @version 1.0.3
 */
@Getter
@SuperBuilder
@RequiredArgsConstructor
public class TaskMessageDelegatorInfo extends RoomMessageDelegatorInfo {
    @JsonProperty("task_id")
    @JsonSerialize(using = ObjectIdSerializer.class)
    @JsonDeserialize(using = ObjectIdDeserializer.class)
    private ObjectId taskId;
    @JsonProperty("task_type")
    private TaskType taskType;
    private boolean result;
    @JsonProperty("err_info")
    @JsonSerialize(using = CodeMessageSerializer.class)
    @JsonDeserialize(using = CodeMessageDeserializer.class)
    private CodeMessage errInfo;
    private Detail detail;

    public interface Detail {}

    public record EngineDetail(
            EngineType type,
            String model
    ) implements Detail {
    }

    public record ChatDetail(
            ChatType type
    ) implements Detail {
    }

    public record ChatbotDetail(
            ChatbotType type,
            String host
    ) implements Detail {
    }

    public record RagDetail(
    ) implements Detail {
    }

    public record VadDetail(
            VadType type,
            String target
    ) implements Detail {
    }

    public record AgentDetail(
            String model
    ) implements Detail {
    }

    @JsonCreator
    public TaskMessageDelegatorInfo(
            @JsonProperty("task_id") ObjectId taskId,
            @JsonProperty("task_type") TaskType taskType,
            @JsonProperty("result") boolean result,
            @JsonProperty("detail") JsonNode detailNode) {
        this.taskId = taskId;
        this.taskType = taskType;
        this.result = result;
        detail = switch(taskType) {
            case ENGINE ->
                    new EngineDetail(EngineType.from(detailNode.get("type").asText()), detailNode.get("model").asText());
            case CHAT -> new ChatDetail(ChatType.from(detailNode.get("type").asText()));
            case CHATBOT -> detailNode.get("model") != null
                    ? new EngineDetail(EngineType.LLM, detailNode.get("model").asText())
                    : new ChatbotDetail(ChatbotType.from(detailNode.get("type").asText()), detailNode.get("host").asText());
            case RAG -> new RagDetail();
            case VAD -> new VadDetail(VadType.from(detailNode.get("type").asText()), detailNode.get("target").asText());
            case AGENT -> new AgentDetail(detailNode.get("model").asText()); };
    }
}
