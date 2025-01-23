package ai.maum.beframework.vo.meta.websocket;

import ai.maum.beframework.vo.meta.Event;
import com.corundumstudio.socketio.SocketIONamespace;
import com.corundumstudio.socketio.listener.ConnectListener;
import com.corundumstudio.socketio.listener.DataListener;
import com.corundumstudio.socketio.listener.DisconnectListener;
import lombok.Getter;

/**
 * 네임스페이스
 * @author baekgol@maum.ai
 */
public class Namespace {
    @Getter
    private final String name;
    private final SocketIONamespace core;

    private Namespace(SocketIONamespace namespace) {
        name = namespace.getName().replaceAll("/", "");
        core = namespace;
    }

    public static Namespace create(SocketIONamespace namespace) {
        return new Namespace(namespace);
    }

    public void setConnectListener(ConnectListener listener) {
        core.addConnectListener(listener);
    }

    public void setDisconnectListener(DisconnectListener listener) {
        core.addDisconnectListener(listener);
    }

    public <T> void addEventListener(Event event, Class<T> clazz, DataListener<T> listener) {
        core.addEventListener(event.getCode(), clazz, listener);
    }
}
