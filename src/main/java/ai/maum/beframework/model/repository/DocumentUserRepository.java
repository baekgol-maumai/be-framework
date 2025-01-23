package ai.maum.beframework.model.repository;

import ai.maum.beframework.model.entity.DocumentUser;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Mono;

/**
 * API 문서 사용자 리포지토리
 * @author baekgol@maum.ai
 */
public interface DocumentUserRepository extends ReactiveMongoRepository<DocumentUser, ObjectId> {
    Mono<DocumentUser> findByAccount(String account);
}
