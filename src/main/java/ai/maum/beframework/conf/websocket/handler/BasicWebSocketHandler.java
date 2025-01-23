package ai.maum.beframework.conf.websocket.handler;

import ai.maum.beframework.codemessage.CodeMessage;
import ai.maum.beframework.codemessage.WebSocketCodeMsg;
import ai.maum.beframework.conf.websocket.WebSocketClientContext;
import ai.maum.beframework.util.StringUtil;
import ai.maum.beframework.vo.BaseException;
import ai.maum.beframework.vo.LogLevel;
import ai.maum.beframework.vo.meta.Event;
import ai.maum.beframework.vo.meta.Message;
import ai.maum.beframework.vo.meta.websocket.Namespace;
import ai.maum.beframework.vo.meta.websocket.WebSocketEvent;
import ai.maum.beframework.vo.meta.websocket.message.ErrorMessage;
import ai.maum.beframework.vo.meta.websocket.message.RoomMessageDelegator;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.listener.ConnectListener;
import com.corundumstudio.socketio.listener.DataListener;
import com.corundumstudio.socketio.listener.DisconnectListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.Nullable;

import java.util.Map;
import java.util.function.Consumer;

/**
 * 기본 웹 소켓 핸들러
 * @author baekgol@maum.ai
 * @version 1.0.0
 */
@Slf4j
public abstract class BasicWebSocketHandler extends WebSocketHandler {
    public BasicWebSocketHandler(Namespace namespace) {
        super(namespace);

        // 일반 요청 이벤트 등록
        if(onError() != null) namespace.addEventListener(WebSocketEvent.ERROR, Object.class, onError());
    }

    /**
     * 웹 소켓 연결 완료 후 초기화
     */
    protected abstract void initAfterConnect(SocketIOClient client);

    /**
     * 웹 소켓 오류 발생 이벤트
     */
    protected abstract DataListener<Object> onError();

    /**
     * 웹 소켓 연결 완료 이벤트
     */
    @Override
    protected ConnectListener onConnect() {
        return client -> {
            if(invalidRoomNamePattern.matcher(getRoomId(client)).matches()) {
                sendError(client, BaseException.of(WebSocketCodeMsg.INVALID_ROOM_NAME), true);
                return;
            }

            WebSocketClientContext.setClient(client);
            print(client, "방 참여", null, LogLevel.INFO);
            initAfterConnect(client);
        };
    }

    /**
     * 웹 소켓 연결 완료 이벤트
     * 연결 완료 후 입력한 로직을 수행할 수 있다.
     */
    protected ConnectListener onConnect(Consumer<SocketIOClient> doOnSuccess) {
        return client -> {
            if(invalidRoomNamePattern.matcher(getRoomId(client)).matches()) {
                sendError(client, BaseException.of(WebSocketCodeMsg.INVALID_ROOM_NAME), true);
                return;
            }

            WebSocketClientContext.setClient(client);
            print(client, "방 참여", null, LogLevel.INFO);

            doOnSuccess.accept(client);
            initAfterConnect(client);
        };
    }

    /**
     * 웹 소켓 연결 해제 이벤트
     */
    @Override
    protected DisconnectListener onDisconnect() {
        return client -> {
            WebSocketClientContext.remove();
            print(client, "방 퇴장", null, LogLevel.INFO);
        };
    }

    /**
     * 웹 소켓 연결 해제 이벤트
     * 연결 해제 완료 후 입력한 로직을 수행할 수 있다.
     */
    protected DisconnectListener onDisconnect(Consumer<SocketIOClient> doOnSuccess) {
        return client -> {
            WebSocketClientContext.remove();
            print(client, "방 퇴장", null, LogLevel.INFO);
            doOnSuccess.accept(client);
        };
    }

    /**
     * 방 ID를 조회한다.
     * @param client 클라이언트
     * @return 방 ID
     */
    @Override
    protected String getRoomId(SocketIOClient client) {
        final String roomId = client.getHandshakeData().getSingleUrlParam("room_id");
        return roomId == null ? "" : roomId;
    }

    /**
     * 사용자 ID를 조회한다.
     * @param client 클라이언트
     * @return 사용자 ID
     */
    @Override
    protected String getUserId(SocketIOClient client) {
        final String userId = client.getHandshakeData().getSingleUrlParam("user_id");
        return userId == null ? "" : userId;
    }

    /**
     * 토큰을 조회한다.
     * @param client 클라이언트
     * @return 토큰
     */
    @Override
    protected String getToken(SocketIOClient client) {
        final Object token = client.getHandshakeData().getAuthToken();
        return token == null ? null : (String)((Map<?, ?>)token).get("token");
    }

    /**
     * 연결된 클라이언트 메시지 송신
     */
    protected void sendToClient(SocketIOClient client, Event event) {
        doSendToClient(client, event, null);
    }

    /**
     * 연결된 클라이언트 메시지 송신
     */
    protected void sendToClient(SocketIOClient client, Event event, Message message) {
        doSendToClient(client, event, message);
    }

    /**
     * 소켓 정보를 포함한 메시지를 출력한다.
     * @param client 클라이언트
     * @param title 메시지 제목
     * @param msg 메시지 내용
     * @param logLevel 로그 레벨
     */
    @Override
    protected void print(SocketIOClient client, String title, Object msg, LogLevel logLevel) {
        final String result = "<"
                + title
                + ">"
                + "\n-------------------------\n"
                + "[Session ID]\n"
                + client.getSessionId()
                + "\n[Room ID]\n"
                + getRoomId(client)
                + "\n[User ID]\n"
                + getUserId(client)
                + (msg == null ? "" : ("\n-------------------------\n" + msg))
                + "\n-------------------------";

        switch(logLevel) {
            case INFO -> log.info(result);
            case WARN -> log.warn(result);
            case ERROR -> log.error(result);
            case DEBUG -> log.debug(result);
            case TRACE -> log.trace(result);
        }
    }

    /**
     * 클라이언트로 오류 메시지 전송한다.
     * 오류 메시지 전송 후 웹 소켓 연결을 해제하도록 설정할 수 있다.
     * @param client 클라이언트
     * @param e 예외
     * @param doDisconnect 오류 메시지 전송 후 웹 소켓 연결 해제 유무
     */
    protected void sendError(SocketIOClient client, Throwable e, boolean doDisconnect) {
        print(client, "오류", e instanceof BaseException ? ((BaseException)e).getInfo() : e.getMessage(), LogLevel.ERROR);
        print(client, "오류 상세", StringUtil.getStackTrace(e), LogLevel.DEBUG);

        final Throwable cause = e.getCause();
        final CodeMessage cm = e instanceof BaseException ? ((BaseException)e).getInfo() : null;

        client.sendEvent(WebSocketEvent.ERROR.getCode(),
                Map.of("code", cm == null ? (cause == null || cause.getStackTrace().length == 0 ? e.getClass().getSimpleName() : cause.getClass().getSimpleName()) : cm.getCode(),
                        "message", cm == null ? (cause == null || cause.getStackTrace().length == 0 ? e.getMessage() : cause.getStackTrace()[0].toString()) : cm.getMessage()));

        if(doDisconnect) client.disconnect();
    }

    protected void setConnectListener() {
        namespace.setConnectListener(onConnect());
    }

    protected void setConnectListener(Consumer<SocketIOClient> doAfterConnect) {
        namespace.setConnectListener(onConnect(doAfterConnect));
    }

    protected void setDisconnectListener() {
        namespace.setDisconnectListener(onDisconnect());
    }

    protected void setDisconnectListener(Consumer<SocketIOClient> doAfterDisconnect) {
        namespace.setDisconnectListener(onDisconnect(doAfterDisconnect));
    }

    protected <T> void addEventListener(Event event, Class<T> clazz, DataListener<T> listener) {
        namespace.addEventListener(event, clazz, listener);
    }

    protected boolean isBypass(SocketIOClient client) {
        final String bypass = client.getHandshakeData().getSingleUrlParam("bypass");
        return bypass != null && bypass.equals("true");
    }

    private void doSendToClient(SocketIOClient client, Event event, @Nullable Message message) {
        switch(message) {
            case null -> client.sendEvent(event.getCode());
            case RoomMessageDelegator<?> rmd -> client.sendEvent(event.getCode(), rmd.resEvent(), rmd.info(), rmd.content());
            case ErrorMessage em -> client.sendEvent(event.getCode(), em);
            default -> client.sendEvent(event.getCode(), message);
        }
    }
}
