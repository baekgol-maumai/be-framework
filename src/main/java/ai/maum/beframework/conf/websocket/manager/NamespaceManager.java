package ai.maum.beframework.conf.websocket.manager;

import ai.maum.beframework.vo.meta.Manager;
import ai.maum.beframework.vo.meta.websocket.Namespace;
import com.corundumstudio.socketio.AuthTokenListener;
import com.corundumstudio.socketio.SocketIONamespace;
import org.springframework.lang.Nullable;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * 네임스페이스 관리자
 * @author baekgol@maum.ai
 */
public class NamespaceManager implements Manager {
    private static final ConcurrentMap<String, Namespace> namespaces = new ConcurrentHashMap<>();

    private NamespaceManager() {}

    /**
     * 네임스페이스를 생성한다.
     * @param namespace 네임스페이스
     * @param authTokenListener 토큰 인증 리스너, nullable
     * @return 네임스페이스 정보
     */
    public static Namespace create(SocketIONamespace namespace, @Nullable AuthTokenListener authTokenListener) {
        final Namespace ns = Namespace.create(namespace);
        namespaces.put(ns.getName(), ns);
        if(authTokenListener != null) namespace.addAuthTokenListener(authTokenListener);
        return ns;
    }
}
