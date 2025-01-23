package ai.maum.beframework.conf.websocket.handler;

import ai.maum.beframework.vo.LogLevel;
import ai.maum.beframework.vo.meta.Event;
import ai.maum.beframework.vo.meta.Message;
import ai.maum.beframework.vo.meta.websocket.Namespace;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.listener.ConnectListener;
import com.corundumstudio.socketio.listener.DisconnectListener;

import java.util.regex.Pattern;

/**
 * 웹 소켓 핸들러 명세 클래스
 * @author baekgol@maum.ai
 * @version 1.0.0
 */
public abstract class WebSocketHandler {
    protected final Namespace namespace;
    protected final Pattern invalidRoomNamePattern = Pattern.compile("^(lock|users).*");

    public WebSocketHandler(Namespace namespace) {
        this.namespace = namespace;
    }

    protected abstract ConnectListener onConnect();
    protected abstract DisconnectListener onDisconnect();
    protected abstract String getRoomId(SocketIOClient client);
    protected abstract String getUserId(SocketIOClient client);
    protected abstract String getToken(SocketIOClient client);
    protected abstract void sendToClient(SocketIOClient client, Event event);
    protected abstract void sendToClient(SocketIOClient client, Event event, Message message);
    protected abstract void print(SocketIOClient client, String title, Object msg, LogLevel logLevel);
}
