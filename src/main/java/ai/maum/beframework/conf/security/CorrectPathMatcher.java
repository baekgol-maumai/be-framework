package ai.maum.beframework.conf.security;

import ai.maum.beframework.conf.properties.DocumentProperties;
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatcher;
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatchers;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * 요청 경로 검증기
 * @author baekgol@maum.ai
 */
public class CorrectPathMatcher implements ServerWebExchangeMatcher {
    private final ServerWebExchangeMatcher excludeMatcher;
    private final ServerWebExchangeMatcher documentMatcher;

    CorrectPathMatcher(String[] excludePattern) {
        this.excludeMatcher = ServerWebExchangeMatchers.pathMatchers(excludePattern);
        documentMatcher = ServerWebExchangeMatchers.pathMatchers(DocumentProperties.paths);
    }

    @Override
    public Mono<MatchResult> matches(ServerWebExchange exchange) {
        return excludeMatcher.matches(exchange)
                .flatMap(mr -> {
                    if(mr.isMatch()) return MatchResult.notMatch();

                    return documentMatcher.matches(exchange)
                            .flatMap(docMr -> {
                                if(docMr.isMatch()) return MatchResult.notMatch();
                                return MatchResult.match();
                            });
                });
    }
}
