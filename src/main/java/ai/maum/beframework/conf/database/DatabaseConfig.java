package ai.maum.beframework.conf.database;

import com.mongodb.reactivestreams.client.MongoClient;
import com.mongodb.reactivestreams.client.MongoClients;
import org.springframework.boot.autoconfigure.mongo.MongoProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.mongodb.ReactiveMongoDatabaseFactory;
import org.springframework.data.mongodb.config.EnableReactiveMongoAuditing;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.SimpleReactiveMongoDatabaseFactory;
import org.springframework.data.mongodb.core.convert.DefaultMongoTypeMapper;
import org.springframework.data.mongodb.core.convert.MappingMongoConverter;
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;

/**
 * DB 설정
 * @author baekgol@maum.ai
 */
@Configuration
@EnableReactiveMongoAuditing
@EnableReactiveMongoRepositories(basePackages = "ai.maum.beframework.model.repository")
public class DatabaseConfig {
    @Bean
    @Primary
    @ConfigurationProperties(prefix = "service.db")
    public MongoProperties mongoProperties() {
        return new MongoProperties();
    }

    @Bean
    @Primary
    public MongoClient mongoClient(MongoProperties mongoProperties) {
        return MongoClients.create(mongoProperties.getUri());
    }

    @Bean
    @Primary
    public ReactiveMongoDatabaseFactory reactiveMongoDatabaseFactory(MongoClient mongoClient, MongoProperties mongoProperties) {
        return new SimpleReactiveMongoDatabaseFactory(mongoClient, mongoProperties.getDatabase());
    }

    @Bean
    @Primary
    public ReactiveMongoTemplate reactiveMongoTemplate(ReactiveMongoDatabaseFactory mongoDatabaseFactory, MappingMongoConverter mappingMongoConverter) {
        mappingMongoConverter.setTypeMapper(new DefaultMongoTypeMapper(null));
        return new ReactiveMongoTemplate(mongoDatabaseFactory, mappingMongoConverter);
    }
}
