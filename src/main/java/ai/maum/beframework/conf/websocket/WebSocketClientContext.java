package ai.maum.beframework.conf.websocket;

import com.corundumstudio.socketio.SocketIOClient;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * 웹 소켓 클라이언트 컨텍스트
 * @author baekgol@maum.ai
 */
public class WebSocketClientContext {
    private static final ThreadLocal<SocketIOClient> holder = new ThreadLocal<>();
    private static final ConcurrentMap<String, SocketIOClient> store = new ConcurrentHashMap<>();

    public static SocketIOClient getClient() {
        return holder.get();
    }

    public static String getSessionId() {
        final SocketIOClient client = getClient();
        if(client == null) return null;
        return client.getSessionId().toString();
    }

    public static void setClient(SocketIOClient client) {
        holder.set(client);
        store.put(client.getSessionId().toString(), client);
    }

    public static void remove() {
        final String sessionId = getSessionId();
        holder.remove();
        if(sessionId != null) store.remove(sessionId);
    }

    public static boolean has(String sessionId) {
        return store.containsKey(sessionId);
    }

    public static List<String> getKeys() {
        return store.values()
                .stream()
                .map(client -> getRoomId(client) + ":" + getUserId(client))
                .toList();
    }

    /**
     * 방 ID 조회
     * @param client 클라이언트
     * @return 방 ID
     */
    private static String getRoomId(SocketIOClient client) {
        final String roomId = client.getHandshakeData().getSingleUrlParam("room_id");
        return roomId == null ? "" : roomId;
    }

    /**
     * 사용자 ID 조회
     * @param client 클라이언트
     * @return 사용자 ID
     */
    private static String getUserId(SocketIOClient client) {
        final String userId = client.getHandshakeData().getSingleUrlParam("user_id");
        return userId == null ? "" : userId;
    }
}
