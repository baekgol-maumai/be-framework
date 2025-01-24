package ai.maum.beframework.conf.websocket.handler;

import ai.maum.beframework.codemessage.SystemCodeMsg;
import ai.maum.beframework.codemessage.WebSocketCodeMsg;
import ai.maum.beframework.conf.websocket.WebSocketClientContext;
import ai.maum.beframework.vo.BaseException;
import ai.maum.beframework.vo.LogLevel;
import ai.maum.beframework.vo.meta.Content;
import ai.maum.beframework.vo.meta.Event;
import ai.maum.beframework.vo.meta.Message;
import ai.maum.beframework.vo.meta.task.TaskMessageDelegatorInfo;
import ai.maum.beframework.vo.meta.task.TaskRequestMessage;
import ai.maum.beframework.vo.meta.task.TaskType;
import ai.maum.beframework.vo.meta.task.chat.ChatType;
import ai.maum.beframework.vo.meta.task.engine.EngineType;
import ai.maum.beframework.vo.meta.type.ContentType;
import ai.maum.beframework.vo.meta.type.DataType;
import ai.maum.beframework.vo.meta.type.ResponseType;
import ai.maum.beframework.vo.meta.type.TextType;
import ai.maum.beframework.vo.meta.type.binary.AudioType;
import ai.maum.beframework.vo.meta.type.binary.BinaryType;
import ai.maum.beframework.vo.meta.type.binary.DocumentType;
import ai.maum.beframework.vo.meta.type.binary.FusionType;
import ai.maum.beframework.vo.meta.type.binary.ImageType;
import ai.maum.beframework.vo.meta.type.binary.VideoType;
import ai.maum.beframework.vo.meta.type.binary.ZipType;
import ai.maum.beframework.vo.meta.websocket.Namespace;
import ai.maum.beframework.vo.meta.websocket.ResponseEvent;
import ai.maum.beframework.vo.meta.websocket.WebSocketEvent;
import ai.maum.beframework.vo.meta.websocket.message.ErrorMessage;
import ai.maum.beframework.vo.meta.websocket.message.RoomMessageDelegator;
import ai.maum.beframework.vo.meta.websocket.message.RoomMessageDelegatorInfo;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.listener.DataListener;
import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.client.SocketOptionBuilder;
import io.socket.emitter.Emitter;
import io.socket.engineio.client.transports.WebSocket;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.lang.Nullable;

import java.net.URISyntaxException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Consumer;

/**
 * 중계 웹 소켓 핸들러
 * @author baekgol@maum.ai
 * @version 1.0.1
 */
@Slf4j
public abstract class RelayWebSocketHandler extends BasicWebSocketHandler {
    private final ConcurrentMap<String, ConcurrentMap<String, Socket>> messengers;

    public RelayWebSocketHandler(Namespace namespace, String target, @Nullable Emitter.Listener doAfterConnectByDest) {
        super(namespace);
        messengers = new ConcurrentHashMap<>();
        setConnectListener(doAfterConnect(target, true, doAfterConnectByDest));
        setDisconnectListener(doAfterDisconnect());
    }

    /**
     * 웹 소켓 오류 발생 이벤트
     */
    @Override
    protected DataListener<Object> onError() {
        return null;
    }

    /**
     * 연결된 클라이언트에 메시지를 전송한다.
     */
    protected void sendToClient(SocketIOClient client, Event event, Object[] args) {
        sendToClient(client, event, convertToJsonObjectByMessage(event, args));
    }

    /**
     * 특정 사용자를 주체로 하여 원격지 웹 소켓 서버로 메시지를 전송한다.
     */
    protected void sendToTarget(String roomId, String userId, Event event) {
        doSendToTarget(roomId, userId, event, null);
    }

    /**
     * 특정 사용자를 주체로 하여 원격지 웹 소켓 서버로 메시지를 전송한다.
     */
    protected void sendToTarget(String roomId, String userId, Event event, Message message) {
        doSendToTarget(roomId, userId, event, message);
    }

    /**
     * 사용자 대상 이벤트 리스너 추가
     */
    protected void addEventListenerByUser(String roomId, String userId, Event event, Emitter.Listener listener) {
        final ConcurrentMap<String, Socket> users = messengers.get(roomId);
        if(users == null) throw BaseException.of(WebSocketCodeMsg.ROOM_ID_NOT_EXIST);

        final Socket user = users.get(userId);
        if(user == null) throw BaseException.of(WebSocketCodeMsg.USER_ID_NOT_EXIST);

        user.on(event.getCode(), listener);
    }

    /**
     * JSONObject 객체를 Message 객체로 변환
     */
    protected Message convertToJsonObjectByMessage(Event event, Object[] args) {
        try {
            if(event instanceof WebSocketEvent wsEvent && wsEvent == WebSocketEvent.ERROR) {
                final JSONObject info = (JSONObject)args[0];
                return new ErrorMessage(info.getString("code"), info.getString("message"));
            }
            else {
                final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");

                return new RoomMessageDelegator<>(
                        ResponseEvent.valueOf((String)args[0]),
                        Optional.ofNullable((JSONObject)args[1])
                                .map(infoNode -> {
                                    try {
                                        final String traceId = infoNode.isNull("trace_id") ? null : infoNode.getString("trace_id");
                                        final String sender = infoNode.getString("sender");
                                        final ResponseType resType = ResponseType.valueOf(infoNode.getString("res_type"));
                                        final LocalDate createdDate = LocalDate.parse(infoNode.getString("created_date"), dateFormatter);
                                        final LocalTime createdTime = LocalTime.parse(infoNode.getString("created_time"), timeFormatter);
                                        final String taskId = infoNode.isNull("task_id") ? null : infoNode.getString("task_id");

                                        if(taskId == null)
                                            return RoomMessageDelegatorInfo.builder()
                                                    .traceId(traceId)
                                                    .sender(sender)
                                                    .resType(resType)
                                                    .createdDate(createdDate)
                                                    .createdTime(createdTime)
                                                    .build();

                                        final JSONObject detailNode = infoNode.getJSONObject("detail");
                                        final TaskType taskType = TaskType.valueOf(infoNode.getString("task_type"));

                                        return TaskMessageDelegatorInfo.builder()
                                                .traceId(traceId)
                                                .sender(sender)
                                                .resType(resType)
                                                .createdDate(createdDate)
                                                .createdTime(createdTime)
                                                .taskId(new ObjectId(taskId))
                                                .taskType(taskType)
                                                .result(infoNode.getBoolean("result"))
                                                .detail(switch(taskType) {
                                                    case ENGINE -> new TaskMessageDelegatorInfo.EngineDetail(
                                                            EngineType.valueOf(detailNode.getString("type")),
                                                            detailNode.getString("model"));
                                                    case CHAT -> new TaskMessageDelegatorInfo.ChatDetail(ChatType.valueOf(detailNode.getString("type")));
                                                    case CHATBOT -> !detailNode.isNull("type")
                                                            ? new TaskMessageDelegatorInfo.EngineDetail(EngineType.LLM, detailNode.getString("model"))
                                                            : new TaskMessageDelegatorInfo.ChatbotDetail(detailNode.getString("host"));
                                                })
                                                .build();
                                    } catch(JSONException e) {
                                        throw BaseException.of(SystemCodeMsg.CONVERT_FAILURE);
                                    }
                                })
                                .orElseThrow(() -> BaseException.of(SystemCodeMsg.CONVERT_FAILURE)),
                        Optional.ofNullable((JSONObject)args[2])
                                .map(contentNode -> {
                                    try {
                                        final DataType dataType = Optional.ofNullable(contentNode.getJSONObject("type"))
                                                .map(typeNode -> {
                                                    try {
                                                        return DataType.valueOf(
                                                                ContentType.Scheme.valueOf(typeNode.getString("scheme")),
                                                                typeNode.getString("format"),
                                                                typeNode.isNull("spec") ? null : typeNode.getString("spec"));
                                                    } catch(JSONException e) {
                                                        throw BaseException.of(SystemCodeMsg.CONVERT_FAILURE);
                                                    }
                                                })
                                                .orElseThrow(() -> BaseException.of(SystemCodeMsg.CONVERT_FAILURE));

                                        return Content.builder()
                                                .type(ContentType.of(dataType))
                                                .data(switch(dataType) {
                                                    case TextType textType -> contentNode.getString("data");
                                                    case BinaryType binaryType -> switch(binaryType) {
                                                        case AudioType audioType -> contentNode.getString("data");
                                                        case DocumentType documentType -> contentNode.getString("data");
                                                        case ImageType imageType -> contentNode.getString("data");
                                                        case VideoType videoType -> contentNode.getString("data");
                                                        case ZipType zipType -> contentNode.getString("data");
                                                        case FusionType fusionType -> Optional.ofNullable(contentNode.getJSONObject("data"))
                                                                .map(dataNode -> {
                                                                    try {
                                                                        return Content.Stf.builder()
                                                                                .audio(dataNode.getString("audio"))
                                                                                .images(Optional.ofNullable(dataNode.getJSONArray("images"))
                                                                                        .map(imagesArray -> {
                                                                                            final List<String> images = new ArrayList<>();

                                                                                            for(int i=0; i<imagesArray.length(); i++) {
                                                                                                try {
                                                                                                    images.add(imagesArray.getString(i));
                                                                                                } catch(JSONException e) {
                                                                                                    throw BaseException.of(SystemCodeMsg.CONVERT_FAILURE);
                                                                                                }
                                                                                            }

                                                                                            return images;
                                                                                        })
                                                                                        .orElseThrow(() -> BaseException.of(SystemCodeMsg.CONVERT_FAILURE)))
                                                                                .build();
                                                                    } catch(JSONException e) {
                                                                        throw BaseException.of(SystemCodeMsg.CONVERT_FAILURE);
                                                                    }
                                                                })
                                                                .orElseThrow(() -> BaseException.of(SystemCodeMsg.CONVERT_FAILURE));
                                                        default -> throw BaseException.of(SystemCodeMsg.CONVERT_FAILURE); };
                                                    default -> throw BaseException.of(SystemCodeMsg.CONVERT_FAILURE);
                                                })
                                                .build();
                                    } catch(JSONException e) {
                                        throw BaseException.of(SystemCodeMsg.CONVERT_FAILURE);
                                    }
                                })
                                .orElse(null));
            }
        } catch(JSONException e) {
            throw BaseException.of(SystemCodeMsg.CONVERT_FAILURE);
        }
    }

    /**
     * Message 객체를 JSONObject 객체로 변환
     */
    protected static JSONObject convertToMessageByJsonObject(Message message) {
        JSONObject o = null;

        if(message instanceof TaskRequestMessage trm)
            o = new JSONObject(Map.of(
                    "input", Optional.ofNullable(trm.tasks().getFirst())
                            .map(firstTask -> switch(firstTask.type()) {
                                case ENGINE -> {
                                    final TaskRequestMessage.TaskInfo.EngineParam ep = (TaskRequestMessage.TaskInfo.EngineParam)firstTask.param();
                                    yield switch(ep.type()) {
                                        case LLM -> ((TaskRequestMessage.TaskInfo.EngineParam.LlmEngineInput)trm.input()).value();
                                        case TTS -> ((TaskRequestMessage.TaskInfo.EngineParam.TtsEngineInput)trm.input()).value();
                                        case STT -> ((TaskRequestMessage.TaskInfo.EngineParam.SttEngineInput)trm.input()).value();
                                        case STF -> ((TaskRequestMessage.TaskInfo.EngineParam.StfEngineInput)trm.input()).value(); }; }
                                case CHAT -> {
                                    final TaskRequestMessage.TaskInfo.ChatParam cp = (TaskRequestMessage.TaskInfo.ChatParam)firstTask.param();
                                    yield switch(cp.type()) {
                                        case GENERAL -> ((TaskRequestMessage.TaskInfo.ChatParam.GeneralChatInput)trm.input()).value();
                                        case NOTICE -> ((TaskRequestMessage.TaskInfo.ChatParam.NoticeChatInput)trm.input()).value(); }; }
                                case CHATBOT -> {
                                    final TaskRequestMessage.TaskInfo.ChatbotParam cbp = (TaskRequestMessage.TaskInfo.ChatbotParam)firstTask.param();
                                    yield ((TaskRequestMessage.TaskInfo.ChatbotParam.CommonChatbotInput)trm.input()).value(); } })
                            .orElseThrow(() -> BaseException.of(SystemCodeMsg.CONVERT_FAILURE)),
                    "tasks", new JSONArray(trm.tasks()
                            .stream()
                            .map(task -> new JSONObject(Map.of(
                                    "trace_id", task.traceId(),
                                    "type", task.type(),
                                    "param", switch(task.type()) {
                                        case ENGINE -> {
                                            final TaskRequestMessage.TaskInfo.EngineParam ep = (TaskRequestMessage.TaskInfo.EngineParam)task.param();
                                            yield new JSONObject(Map.of(
                                                    "type", ep.type(),
                                                    "model", ep.model(),
                                                    "config", switch(ep.type()) {
                                                        case LLM -> {
                                                            final TaskRequestMessage.TaskInfo.EngineParam.LlmEngineConfig lec = (TaskRequestMessage.TaskInfo.EngineParam.LlmEngineConfig)ep.config();
                                                            yield new JSONObject(Map.of(
                                                                    "top_p", lec.topP(),
                                                                    "top_k", lec.topK(),
                                                                    "temperature", lec.temperature(),
                                                                    "presence_penalty", lec.presencePenalty(),
                                                                    "frequency_penalty", lec.frequencyPenalty(),
                                                                    "beam_width", lec.beamWidth())); }
                                                        case TTS -> {
                                                            final TaskRequestMessage.TaskInfo.EngineParam.TtsEngineConfig tec = (TaskRequestMessage.TaskInfo.EngineParam.TtsEngineConfig)ep.config();
                                                            yield new JSONObject(Map.of(
                                                                    "lang", tec.lang(),
                                                                    "sample_rate", tec.sampleRate(),
                                                                    "speaker", tec.speaker(),
                                                                    "audio_encoding", tec.audioEncoding(),
                                                                    "duration_rate", tec.durationRate(),
                                                                    "emotion", tec.emotion(),
                                                                    "padding", new JSONObject(Map.of(
                                                                            "begin", tec.padding().begin(),
                                                                            "end", tec.padding().end())),
                                                                    "profile", tec.profile(),
                                                                    "speaker_name", tec.speakerName())); }
                                                        case STT -> {
                                                            final TaskRequestMessage.TaskInfo.EngineParam.SttEngineConfig sttec = (TaskRequestMessage.TaskInfo.EngineParam.SttEngineConfig)ep.config();
                                                            yield new JSONObject(); }
                                                        case STF -> {
                                                            final TaskRequestMessage.TaskInfo.EngineParam.StfEngineConfig stfec = (TaskRequestMessage.TaskInfo.EngineParam.StfEngineConfig)ep.config();
                                                            yield new JSONObject(); } })); }
                                        case CHAT -> {
                                            final TaskRequestMessage.TaskInfo.ChatParam cp = (TaskRequestMessage.TaskInfo.ChatParam)task.param();
                                            yield new JSONObject(Map.of(
                                                    "type", cp.type(),
                                                    "config", switch(cp.type()) {
                                                        case GENERAL -> {
                                                            final TaskRequestMessage.TaskInfo.ChatParam.GeneralChatConfig gcc = (TaskRequestMessage.TaskInfo.ChatParam.GeneralChatConfig)cp.config();
                                                            yield new JSONObject(); }
                                                        case NOTICE -> {
                                                            final TaskRequestMessage.TaskInfo.ChatParam.NoticeChatConfig ncc = (TaskRequestMessage.TaskInfo.ChatParam.NoticeChatConfig)cp.config();
                                                            yield new JSONObject(); } })); }
                                        case CHATBOT -> {
                                            final TaskRequestMessage.TaskInfo.ChatbotParam cbp = (TaskRequestMessage.TaskInfo.ChatbotParam)task.param();
                                            final TaskRequestMessage.TaskInfo.ChatbotParam.CommonChatbotConfig ccbc = (TaskRequestMessage.TaskInfo.ChatbotParam.CommonChatbotConfig)cbp.config();
                                            yield new JSONObject(Map.of(
                                                    "host", cbp.host(),
                                                    "config", new JSONObject())); } })))
                            .toList()),
                    "send_last_only", trm.sendLastOnly()));
        else if(message instanceof RoomMessageDelegator<?> rmd)
            o = new JSONObject(Map.of(
                    "res_event", rmd.resEvent(),
                    "info", Optional.ofNullable(rmd.info())
                            .map(info -> {
                                if(info instanceof TaskMessageDelegatorInfo tmdi)
                                    return new JSONObject();

                                return new JSONObject();
                            })
                            .orElseThrow(() -> BaseException.of(SystemCodeMsg.CONVERT_FAILURE)),
                    "content", new JSONObject(Map.of(
                            "type", Optional.ofNullable(rmd.content().getType())
                                    .map(contentType -> {
                                        final DataType dataType = contentType.getDataType();
                                        final Map<String, String> typeInfo = new HashMap<>();

                                        typeInfo.put("scheme", dataType.getScheme());
                                        typeInfo.put("format", dataType.getFormat());

                                        if(dataType instanceof BinaryType binaryType)
                                            typeInfo.put("spec", binaryType.getSpec());

                                        return new JSONObject(typeInfo);
                                    })
                                    .orElseThrow(() -> BaseException.of(SystemCodeMsg.CONVERT_FAILURE)),
                            "data", Optional.ofNullable(rmd.content().getType().getDataType())
                                    .map(dataType -> {
                                        if(dataType instanceof TextType)
                                            return rmd.content().getData();
                                        else if(dataType instanceof BinaryType binaryType) {
                                            if(binaryType instanceof AudioType)
                                                return rmd.content().getData();
                                            else if(binaryType instanceof DocumentType)
                                                return rmd.content().getData();
                                            else if(binaryType instanceof ImageType)
                                                return rmd.content().getData();
                                            else if(binaryType instanceof VideoType)
                                                return rmd.content().getData();
                                            else if(binaryType instanceof ZipType)
                                                return rmd.content().getData();
                                            else if(binaryType instanceof FusionType) {
                                                final Content.Stf stfData = (Content.Stf)rmd.content().getData();
                                                return new JSONObject(Map.of(
                                                        "audio", stfData.getAudio(),
                                                        "images", new JSONArray(stfData.getImages())));
                                            }
                                        }

                                        return null;
                                    })
                                    .orElseThrow(() -> BaseException.of(SystemCodeMsg.CONVERT_FAILURE))))));

        return o;
    }

    private void doSendToTarget(String roomId, String userId, Event event, @Nullable Message message) {
        final ConcurrentMap<String, Socket> users = messengers.get(roomId);
        if(users == null) throw BaseException.of(WebSocketCodeMsg.ROOM_ID_NOT_EXIST);

        final Socket user = users.get(userId);
        if(user == null) throw BaseException.of(WebSocketCodeMsg.USER_ID_NOT_EXIST);

        if(message == null) user.emit(event.getCode());
        else user.emit(event.getCode(), convertToMessageByJsonObject(message));
    }

    private Consumer<SocketIOClient> doAfterConnect(String target, boolean isLog, @Nullable Emitter.Listener doAfterConnectByDest) {
        return client -> {
            final String roomId = getRoomId(client);
            final String userId = getUserId(client);
            final String token = getToken(client);

            try {
                final SocketOptionBuilder builder = IO.Options.builder()
                        .setTransports(new String[] { WebSocket.NAME })
                        .setUpgrade(true)
                        .setPath("/socket.io/")
                        .setReconnection(true)
                        .setReconnectionAttempts(Integer.MAX_VALUE)
                        .setReconnectionDelay(3000)
                        .setQuery(client.getHandshakeData().getUrlParams().entrySet()
                                .stream()
                                .filter(e -> !e.getKey().equals("EIO") && !e.getKey().equals("transport"))
                                .reduce("", (r, v) -> r + (r.isEmpty() ? "" : "&") + v.getKey() + "=" + v.getValue().getFirst(), (c1, c2) -> c1)
                                + "&log=" + isLog);

                if(token != null) builder.setAuth(Map.of("token", token));

                final Socket messenger = IO.socket(target, builder.build()).connect();
                final ConcurrentMap<String, Socket> users = messengers.getOrDefault(roomId, new ConcurrentHashMap<>());

                users.put(userId, messenger);
                messengers.putIfAbsent(roomId, users);

                // 연결 완료 이벤트
                if(doAfterConnectByDest != null)
                    messenger.on(WebSocketEvent.CONNECT.getCode(), doAfterConnectByDest);

                // 사용자 참여 이벤트
                messenger.on(
                        WebSocketEvent.JOIN.getCode(),
                        args -> sendToClient(client, WebSocketEvent.JOIN, convertToJsonObjectByMessage(WebSocketEvent.JOIN, args)));

                // 사용자 퇴장 이벤트
                messenger.on(
                        WebSocketEvent.LEAVE.getCode(),
                        args -> {
                            sendToClient(client, WebSocketEvent.LEAVE, convertToJsonObjectByMessage(WebSocketEvent.LEAVE, args));

                            try {
                                // 방을 퇴장한 사용자가 자신일 경우
                                if(((JSONObject)args[2]).getString("data").equals(userId)) {
                                    WebSocketClientContext.remove();
                                    print(client, "방 퇴장", null, LogLevel.INFO);
                                }
                            } catch(JSONException e) {
                                throw BaseException.of(WebSocketCodeMsg.USER_ID_NOT_EXIST);
                            }
                        });

                // 오류 발생 이벤트
                messenger.on(
                        WebSocketEvent.ERROR.getCode(),
                        args -> {
                            final ErrorMessage message = (ErrorMessage) convertToJsonObjectByMessage(WebSocketEvent.ERROR, args);

                            sendToClient(client, WebSocketEvent.ERROR, message);

                            if(message.code().equals("WS005")) {
                                messenger.disconnect();
                                client.disconnect();
                            }
                        });
            } catch(URISyntaxException e) {
                throw BaseException.of(e);
            }
        };
    }

    private Consumer<SocketIOClient> doAfterDisconnect() {
        return client -> {
            // 사용자 측에서 연결 해제하였을 경우
            // 원격지에 연결이 해제되었음을 전파
            sendToTarget(getRoomId(client), getUserId(client), WebSocketEvent.USER_DISCONNECT);
            WebSocketClientContext.remove();
            print(client, "방 퇴장", null, LogLevel.INFO);
        };
    }
}
