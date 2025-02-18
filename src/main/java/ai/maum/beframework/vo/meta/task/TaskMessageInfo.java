package ai.maum.beframework.vo.meta.task;

import ai.maum.beframework.conf.serialization.deserializer.ObjectIdDeserializer;
import ai.maum.beframework.conf.serialization.serializer.ObjectIdSerializer;
import ai.maum.beframework.vo.meta.task.chat.ChatType;
import ai.maum.beframework.vo.meta.task.engine.EngineType;
import ai.maum.beframework.vo.meta.websocket.message.RoomMessageInfo;
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
 * 작업 메시지 정보
 * @author baekgol@maum.ai
 * @version 1.0.0
 */
@Getter
@SuperBuilder
@RequiredArgsConstructor
public class TaskMessageInfo extends RoomMessageInfo {
    @JsonProperty("task_id")
    @JsonSerialize(using = ObjectIdSerializer.class)
    @JsonDeserialize(using = ObjectIdDeserializer.class)
    private ObjectId taskId;
    @JsonProperty("task_type")
    private TaskType taskType;
    private boolean result;
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
            String host
    ) implements Detail {
    }

    public record RagDetail(
    ) implements Detail {
    }

    @JsonCreator
    public TaskMessageInfo(
            @JsonProperty("task_id") ObjectId taskId,
            @JsonProperty("task_type") TaskType taskType,
            @JsonProperty("result") boolean result,
            @JsonProperty("detail") JsonNode detailNode) {
        this.taskId = taskId;
        this.taskType = taskType;
        this.result = result;
        detail = switch(taskType) {
            case ENGINE -> new TaskMessageInfo.EngineDetail(EngineType.from(detailNode.get("type").asText()), detailNode.get("model").asText());
            case CHAT -> new TaskMessageInfo.ChatDetail(ChatType.from(detailNode.get("type").asText()));
            case CHATBOT -> detailNode.get("type") != null
                    ? new TaskMessageInfo.EngineDetail(EngineType.LLM, detailNode.get("model").asText())
                    : new TaskMessageInfo.ChatbotDetail(detailNode.get("host").asText());
            case RAG -> new TaskMessageInfo.RagDetail(); };
    }
}
