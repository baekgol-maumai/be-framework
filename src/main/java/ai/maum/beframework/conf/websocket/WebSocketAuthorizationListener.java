package ai.maum.beframework.conf.websocket;

import com.corundumstudio.socketio.AuthorizationListener;
import com.corundumstudio.socketio.AuthorizationResult;
import com.corundumstudio.socketio.HandshakeData;

/**
 * 웹 소켓 인증 리스너
 * @author baekgol@maum.ai
 */
public class WebSocketAuthorizationListener implements AuthorizationListener {
    @Override
    public AuthorizationResult getAuthorizationResult(HandshakeData data) {
        return AuthorizationResult.SUCCESSFUL_AUTHORIZATION;
    }
}
