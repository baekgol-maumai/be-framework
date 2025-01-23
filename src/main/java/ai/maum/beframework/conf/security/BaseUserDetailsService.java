package ai.maum.beframework.conf.security;

import ai.maum.beframework.model.repository.DocumentUserRepository;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import reactor.core.publisher.Mono;

/**
 * 기본 사용자 인증 서비스
 * @author baekgol@maum.ai
 */
public class BaseUserDetailsService implements ReactiveUserDetailsService {
    private final DocumentUserRepository documentUserRepository;

    BaseUserDetailsService(DocumentUserRepository documentUserRepository) {
        this.documentUserRepository = documentUserRepository;
    }

    @Override
    public Mono<UserDetails> findByUsername(String username) {
        return documentUserRepository.findByAccount(username)
                .map(du -> BaseUserDetails.builder()
                        .username(username)
                        .password(du.getPassword())
                        .build());
    }
}
