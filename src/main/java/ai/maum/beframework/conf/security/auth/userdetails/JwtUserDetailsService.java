package ai.maum.beframework.conf.security.auth.userdetails;

import ai.maum.beframework.model.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import reactor.core.publisher.Mono;

/**
 * JWT 사용자 인증 서비스
 * @author baekgol@maum.ai
 */
public class JwtUserDetailsService extends BaseUserDetailsService {
    private final UserRepository userRepository;

    public JwtUserDetailsService(UserRepository userRepository) {
        super();
        this.userRepository = userRepository;
    }

    @Override
    public Mono<UserDetails> findByUsername(String username) {
        return userRepository.findByEmail(username)
                .map(u -> JwtUserDetails.builder()
                        .username(username)
                        .password(u.getPassword())
                        .userId(u.getId())
                        .build());
    }
}
