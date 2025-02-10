package ai.maum.beframework.model.repository;

import ai.maum.beframework.model.entity.User;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Mono;

/**
 * 사용자 리포지토리
 * @author baekgol@maum.ai
 */
public interface UserRepository extends ReactiveMongoRepository<User, ObjectId> {
    Mono<User> findByEmail(String email);
}
