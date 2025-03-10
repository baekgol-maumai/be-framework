package ai.maum.beframework.vo.meta.websocket.message;

import ai.maum.beframework.conf.serialization.deserializer.LocalDateDeserializer;
import ai.maum.beframework.conf.serialization.deserializer.LocalTimeDeserializer;
import ai.maum.beframework.conf.serialization.serializer.LocalDateSerializer;
import ai.maum.beframework.conf.serialization.serializer.LocalTimeSerializer;
import ai.maum.beframework.vo.meta.type.ResponseType;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * 방 메시지 정보
 * @author baekgol@maum.ai
 * @version 1.0.0
 */
@Getter
@SuperBuilder
@RequiredArgsConstructor
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.EXTERNAL_PROPERTY, property = "msg_type")
public class RoomMessageInfo implements MessageInfo {
    @JsonProperty("trace_id")
    private String traceId;
    private String sender;
    @JsonProperty("res_type")
    private ResponseType resType;
    @JsonProperty("created_date")
    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    private LocalDate createdDate;
    @JsonProperty("created_time")
    @JsonSerialize(using = LocalTimeSerializer.class)
    @JsonDeserialize(using = LocalTimeDeserializer.class)
    private LocalTime createdTime;
}
