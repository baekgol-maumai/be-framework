package ai.maum.beframework.conf.security.auth.userdetails;

import ai.maum.beframework.model.repository.DocumentUserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import reactor.core.publisher.Mono;

/**
 * API 문서 사용자 인증 서비스
 * @author baekgol@maum.ai
 */
public class DocumentUserDetailsService extends BaseUserDetailsService {
    private final DocumentUserRepository documentUserRepository;

    public DocumentUserDetailsService(DocumentUserRepository documentUserRepository) {
        super();
        this.documentUserRepository = documentUserRepository;
    }

    @Override
    public Mono<UserDetails> findByUsername(String username) {
        return documentUserRepository.findByAccount(username)
                .map(du -> DocumentUserDetails.builder()
                        .username(username)
                        .password(du.getPassword())
                        .build());
    }
}
