package ai.maum.beframework.vo.meta.router;

import ai.maum.beframework.codemessage.SystemCodeMsg;
import ai.maum.beframework.conf.properties.SecurityProperties;
import ai.maum.beframework.util.SecurityUtil;
import ai.maum.beframework.vo.BaseException;
import org.springframework.http.HttpMethod;
import org.springframework.web.reactive.function.server.HandlerFunction;
import org.springframework.web.reactive.function.server.RequestPredicate;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.util.pattern.PathPatternParser;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;
import reactor.util.function.Tuples;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

/**
 * 라우터 빌더
 * @author baekgol@maum.ai
 */
public class RouterBuilder {
    private final List<Object> list;
    private final Map<String, Map<HttpMethod, String[]>> roleMappings;
    private final PathPatternParser pathParser = new PathPatternParser();

    private RouterBuilder() {
        list = new ArrayList<>();
        roleMappings = new HashMap<>();
    }

    public static RouterBuilder create() {
        return new RouterBuilder();
    }

    public RouterFunction<ServerResponse> build() {
        RouterFunctions.Builder builder = RouterFunctions.route();

        for(Object e: list) {
            if(e instanceof Map<?, ?> info) {
                final String path = (String)info.get("path");
                final Map<HttpMethod, String[]> roleMapping = roleMappings.getOrDefault(path, new HashMap<>());

                if(roleMapping.isEmpty())
                    roleMappings.put(path, roleMapping);

                roleMapping.put((HttpMethod)info.get("method"), (String[])info.get("roles"));

                builder = build(info, builder);
            }
            else if(e instanceof Tuple2<?, ?> info)
                builder = nestBuild("", (String)info.getT1(), (RouterBuilder)info.getT2(), builder);
        }

        return builder.build()
                .filter((request, next) -> {
                    Map<HttpMethod, String[]> roleMapping = roleMappings.get(request.path());

                    if(roleMapping == null) {
                        boolean isFind = false;

                        for(Map.Entry<String, Map<HttpMethod, String[]>> e: roleMappings.entrySet()) {
                            if(pathParser.parse(e.getKey()).matches(request.requestPath())) {
                                isFind = true;
                                roleMapping = e.getValue();
                                break;
                            }
                        }

                        if(!isFind)
                            return Mono.error(BaseException.of(SystemCodeMsg.ACCESS_DENIED));
                    }

                    final String[] roles = roleMapping.get(request.method());

                    if(roles == null || roles.length == 0)
                        return next.handle(request);

                    return SecurityUtil.getAuthentication()
                            .filter(ud -> {
                                if(ud.getAuthorities() == null)
                                    return false;

                                return ud.getAuthorities()
                                        .stream()
                                        .anyMatch(auth -> Arrays.stream(roles).anyMatch(role -> auth.getAuthority().equals(role)));
                            })
                            .switchIfEmpty(Mono.error(BaseException.of(SystemCodeMsg.ACCESS_DENIED)))
                            .flatMap(ud -> next.handle(request));
                });
    }

    public RouterBuilder GET(String path,
                             HandlerFunction<ServerResponse> handlerFunction) {
        return GET(path, null, handlerFunction, SecurityProperties.ROLE_BASIC);
    }

    public RouterBuilder GET(String path,
                             RequestPredicate predicate,
                             HandlerFunction<ServerResponse> handlerFunction) {
        return GET(path, predicate, handlerFunction, SecurityProperties.ROLE_BASIC);
    }

    public RouterBuilder GET(String path,
                             HandlerFunction<ServerResponse> handlerFunction,
                             String... roles) {
        return GET(path, null, handlerFunction, roles);
    }

    public RouterBuilder GET(String path,
                             RequestPredicate predicate,
                             HandlerFunction<ServerResponse> handlerFunction,
                             String... roles) {
        return add(HttpMethod.GET, path, predicate, handlerFunction, roles);
    }

    public RouterBuilder POST(String path,
                              HandlerFunction<ServerResponse> handlerFunction) {
        return POST(path, null, handlerFunction, SecurityProperties.ROLE_BASIC);
    }

    public RouterBuilder POST(String path,
                              RequestPredicate predicate,
                              HandlerFunction<ServerResponse> handlerFunction) {
        return POST(path, predicate, handlerFunction, SecurityProperties.ROLE_BASIC);
    }

    public RouterBuilder POST(String path,
                              HandlerFunction<ServerResponse> handlerFunction,
                              String... roles) {
        return POST(path, null, handlerFunction, roles);
    }

    public RouterBuilder POST(String path,
                              RequestPredicate predicate,
                              HandlerFunction<ServerResponse> handlerFunction,
                              String... roles) {
        return add(HttpMethod.POST, path, predicate, handlerFunction, roles);
    }

    public RouterBuilder PUT(String path,
                             HandlerFunction<ServerResponse> handlerFunction) {
        return PUT(path, null, handlerFunction, SecurityProperties.ROLE_BASIC);
    }

    public RouterBuilder PUT(String path,
                             RequestPredicate predicate,
                             HandlerFunction<ServerResponse> handlerFunction) {
        return PUT(path, predicate, handlerFunction, SecurityProperties.ROLE_BASIC);
    }

    public RouterBuilder PUT(String path,
                             HandlerFunction<ServerResponse> handlerFunction,
                             String... roles) {
        return PUT(path, null, handlerFunction, roles);
    }

    public RouterBuilder PUT(String path,
                             RequestPredicate predicate,
                             HandlerFunction<ServerResponse> handlerFunction,
                             String... roles) {
        return add(HttpMethod.PUT, path, predicate, handlerFunction, roles);
    }

    public RouterBuilder PATCH(String path,
                               HandlerFunction<ServerResponse> handlerFunction) {
        return PATCH(path, null, handlerFunction, SecurityProperties.ROLE_BASIC);
    }

    public RouterBuilder PATCH(String path,
                               RequestPredicate predicate,
                               HandlerFunction<ServerResponse> handlerFunction) {
        return PATCH(path, predicate, handlerFunction, SecurityProperties.ROLE_BASIC);
    }

    public RouterBuilder PATCH(String path,
                               HandlerFunction<ServerResponse> handlerFunction,
                               String... roles) {
        return PATCH(path, null, handlerFunction, roles);
    }

    public RouterBuilder PATCH(String path,
                               RequestPredicate predicate,
                               HandlerFunction<ServerResponse> handlerFunction,
                               String... roles) {
        return add(HttpMethod.PATCH, path, predicate, handlerFunction, roles);
    }

    public RouterBuilder DELETE(String path,
                                HandlerFunction<ServerResponse> handlerFunction) {
        return DELETE(path, null, handlerFunction, SecurityProperties.ROLE_BASIC);
    }

    public RouterBuilder DELETE(String path,
                                RequestPredicate predicate,
                                HandlerFunction<ServerResponse> handlerFunction) {
        return DELETE(path, predicate, handlerFunction, SecurityProperties.ROLE_BASIC);
    }

    public RouterBuilder DELETE(String path,
                                HandlerFunction<ServerResponse> handlerFunction,
                                String... roles) {
        return DELETE(path, null, handlerFunction, roles);
    }

    public RouterBuilder DELETE(String path,
                                RequestPredicate predicate,
                                HandlerFunction<ServerResponse> handlerFunction,
                                String... roles) {
        return add(HttpMethod.DELETE, path, predicate, handlerFunction, roles);
    }

    public RouterBuilder nest(String path, Function<RouterBuilder, RouterBuilder> nestBuilder) {
        list.add(Tuples.of(path, nestBuilder.apply(new RouterBuilder())));
        return this;
    }

    private RouterBuilder add(HttpMethod method,
                              String path,
                              RequestPredicate predicate,
                              HandlerFunction<ServerResponse> handlerFunction,
                              String... roles) {
        final Map<String, Object> info = new HashMap<>();

        info.put("method", method);
        info.put("path", path);
        info.put("handler_function", handlerFunction);

        if(predicate != null) info.put("predicate", predicate);
        if(roles != null && roles.length > 0) info.put("roles", roles);

        list.add(info);

        return this;
    }

    @SuppressWarnings("unchecked")
    private RouterFunctions.Builder build(Map<?, ?> info, RouterFunctions.Builder builder) {
        final HttpMethod method = (HttpMethod)info.get("method");
        final String path = (String)info.get("path");
        final RequestPredicate predicate = (RequestPredicate)info.get("predicate");
        final String[] roles = (String[])info.get("roles");
        final HandlerFunction<ServerResponse> handlerFunction = (HandlerFunction<ServerResponse>)info.get("handler_function");

        return (switch(method.name()) {
            case "GET" -> {
                if(predicate == null) yield builder.GET(path, handlerFunction);
                yield builder.GET(path, predicate, handlerFunction); }
            case "POST" -> {
                if(predicate == null) yield builder.POST(path, handlerFunction);
                yield builder.POST(path, predicate, handlerFunction); }
            case "PUT" -> {
                if(predicate == null) yield builder.PUT(path, handlerFunction);
                yield builder.PUT(path, predicate, handlerFunction); }
            case "PATCH" -> {
                if(predicate == null) yield builder.PATCH(path, handlerFunction);
                yield builder.PATCH(path, predicate, handlerFunction); }
            case "DELETE" -> {
                if(predicate == null) yield builder.DELETE(path, handlerFunction);
                yield builder.DELETE(path, predicate, handlerFunction); }
            default -> throw BaseException.of(SystemCodeMsg.FAILURE); });
    }

    private RouterFunctions.Builder nestBuild(String currPath, String mainPath, RouterBuilder childBuilder, RouterFunctions.Builder parentBuilder) {
        return parentBuilder.path(mainPath, builder -> childBuilder.list.forEach(e -> {
            if(e instanceof Map<?, ?> info) {
                final String fullPath = currPath + mainPath + info.get("path");
                final Map<HttpMethod, String[]> roleMapping = roleMappings.getOrDefault(fullPath, new HashMap<>());

                if(roleMapping.isEmpty())
                    roleMappings.put(fullPath, roleMapping);

                roleMapping.put((HttpMethod)info.get("method"), (String[])info.get("roles"));

                build(info, builder);
            }
            else if(e instanceof Tuple2<?, ?> info)
                nestBuild(currPath + mainPath, (String)info.getT1(), (RouterBuilder)info.getT2(), builder);
        }));
    }
}
