package ai.maum.beframework.util;

import ai.maum.beframework.codemessage.CodeMessage;
import ai.maum.beframework.conf.websocket.WebSocketClientContext;
import ai.maum.beframework.vo.BaseException;
import ai.maum.beframework.vo.LogLevel;
import ai.maum.beframework.vo.meta.websocket.WebSocketEvent;
import com.corundumstudio.socketio.SocketIOClient;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

/**
 * 웹 소켓 클라이언트 유틸
 * @author baekgol@maum.ai
 */
@Slf4j
public class WebSocketClientUtil {
    private WebSocketClientUtil() {}

    public static void sendError(BaseException e) {
        final CodeMessage cm = e.getInfo();
        final SocketIOClient client = WebSocketClientContext.getClient();

        print(client, "오류", cm, LogLevel.ERROR);
        print(client, "오류 상세", StringUtil.getStackTrace(e), LogLevel.DEBUG);

        client.sendEvent(WebSocketEvent.ERROR.getCode(),
                Map.of("code", cm.getCode(), "message", cm.getMessage()),
                Map.of("room_id", getRoomId(client), "user_id", getUserId(client)));
    }

    public static void print(SocketIOClient client, String title, Object msg, LogLevel logLevel) {
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
                + "\n-------------------------\n";

        switch(logLevel) {
            case INFO -> log.info(result);
            case WARN -> log.warn(result);
            case ERROR -> log.error(result);
            case DEBUG -> log.debug(result);
            case TRACE -> log.trace(result);
        }
    }

    public static String getUserId(SocketIOClient client) {
        final String userId = client.getHandshakeData().getSingleUrlParam("user_id");
        return userId == null ? "" : userId;
    }

    public static String getRoomId(SocketIOClient client) {
        final String roomId = client.getHandshakeData().getSingleUrlParam("room_id");
        return roomId == null ? "" : roomId;
    }
}
