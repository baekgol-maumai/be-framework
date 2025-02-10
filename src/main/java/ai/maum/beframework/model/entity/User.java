package ai.maum.beframework.model.entity;

import ai.maum.beframework.vo.meta.Entity;
import lombok.Builder;
import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.MongoId;

/**
 * 사용자 엔티티
 * @author baekgol@maum.ai
 */
@Data
@Builder
@Document
public class User implements Entity {
    @MongoId
    private ObjectId id;
    private String password;
    private String email;
    private String name;
    @Field("company_id")
    private String companyId;
}
