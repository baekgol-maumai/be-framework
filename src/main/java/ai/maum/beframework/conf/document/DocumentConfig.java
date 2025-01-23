package ai.maum.beframework.conf.document;

import ai.maum.beframework.conf.properties.DocumentProperties;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;

import java.util.stream.Collectors;

/**
 * API 문서 설정
 * @author baekgol@maum.ai
 */
@Configuration
@RequiredArgsConstructor
public class DocumentConfig {
    private final DocumentProperties documentProperties;

    @Bean
    public OpenAPI openApi() {
        return new OpenAPI()
                .info(new Info()
                        .title(documentProperties.getTitle())
                        .description(documentProperties.getDesc()))
                .servers(documentProperties.getServerUrl()
                        .stream()
                        .map(e -> new Server().url(e))
                        .collect(Collectors.toList()))
                .schemaRequirement("JWT", new SecurityScheme()
                        .name(HttpHeaders.AUTHORIZATION)
                        .in(SecurityScheme.In.HEADER)
                        .type(SecurityScheme.Type.HTTP)
                        .scheme("bearer")
                        .bearerFormat("JWT")
                        .description("인증 및 인가된 사용자만 접근 할 수 있도록 한다."))
                .addSecurityItem(new SecurityRequirement().addList("JWT"));
    }
}
