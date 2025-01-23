package ai.maum.beframework;

import com.corundumstudio.socketio.SocketIOServer;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 애플리케이션 메인
 * @author baekgol@maum.ai
 */
@SpringBootApplication
@RequiredArgsConstructor
public class BeFrameworkApplication implements CommandLineRunner, DisposableBean {
    private final SocketIOServer websocketServer;

    public static void main(String[] args) {
        SpringApplication.run(BeFrameworkApplication.class, args);
    }

    @Override
    public void run(String... args) {
        // 웹 소켓 서버 시작
        websocketServer.start();
    }

    @Override
    public void destroy() {
        // 웹 소켓 서버 종료
        websocketServer.stop();
    }
}
