package ai.maum.beframework.conf.websocket;

import com.corundumstudio.socketio.SocketConfig;
import com.corundumstudio.socketio.SocketIOServer;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 웹 소켓 설정
 * @author baekgol@maum.ai
 */
@Configuration
@RequiredArgsConstructor
public class WebSocketConfig {
    @Value("${service.websocket.port}")
    private int port;

    @Bean
    public SocketIOServer socketIoServer() {
        final com.corundumstudio.socketio.Configuration config = new com.corundumstudio.socketio.Configuration();
        final SocketConfig skConfig = new SocketConfig();

        skConfig.setReuseAddress(true);
        config.setPort(port);
        config.setMaxFramePayloadLength(1024 * 1024 * 3);
        config.setSocketConfig(skConfig);

        return new SocketIOServer(config);
    }
}
