package ai.maum.beframework.conf.security.auth.userdetails;

import ai.maum.beframework.model.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import reactor.core.publisher.Mono;

/**
 * 주요 사용자 인증 서비스
 * @author baekgol@maum.ai
 */
public class MainUserDetailsService extends BaseUserDetailsService {
    private final UserRepository userRepository;

    public MainUserDetailsService(UserRepository userRepository) {
        super();
        this.userRepository = userRepository;
    }

    @Override
    public Mono<UserDetails> findByUsername(String username) {
        return userRepository.findByEmail(username)
                .map(u -> MainUserDetails.builder()
                        .username(username)
                        .password(u.getPassword())
                        .userId(u.getId())
                        .build());
    }
}
