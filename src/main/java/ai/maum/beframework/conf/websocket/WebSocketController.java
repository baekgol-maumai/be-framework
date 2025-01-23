package ai.maum.beframework.conf.websocket;

import ai.maum.beframework.conf.websocket.handler.WebSocketHandler;
import com.corundumstudio.socketio.SocketIOServer;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.ToString;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * 웹 소켓 컨트롤러
 * @author baekgol@maum.ai
 */
@Configuration
@Getter
@ToString
public class WebSocketController {
    private final SocketIOServer server;
    private final ConcurrentMap<String, WebSocketHandler> handlers;

    WebSocketController(SocketIOServer server, ObjectMapper objectMapper) {
        this.server = server;
        handlers = new ConcurrentHashMap<>();
    }
}
