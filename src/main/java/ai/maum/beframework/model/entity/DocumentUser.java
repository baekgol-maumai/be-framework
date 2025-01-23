package ai.maum.beframework.model.entity;

import ai.maum.beframework.vo.AuditField;
import ai.maum.beframework.vo.meta.Entity;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

/**
 * API 문서 사용자 엔티티
 * @author baekgol@maum.ai
 */
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Document("document_user")
public class DocumentUser extends AuditField implements Entity {
    @MongoId
    private ObjectId id;
    private String account;
    private String password;
}
