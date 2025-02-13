package ai.maum.beframework.conf.security;

import ai.maum.beframework.conf.properties.SecurityProperties;
import ai.maum.beframework.conf.security.auth.manager.AnonymousAuthenticationManager;
import ai.maum.beframework.conf.security.auth.manager.DocumentAuthenticationManager;
import ai.maum.beframework.conf.security.auth.manager.JwtAuthenticationManager;
import ai.maum.beframework.conf.security.auth.userdetails.AnonymousUserDetailsService;
import ai.maum.beframework.conf.security.auth.userdetails.DocumentUserDetailsService;
import ai.maum.beframework.conf.security.auth.userdetails.JwtUserDetailsService;
import ai.maum.beframework.conf.security.encoder.AnonymousPasswordEncoder;
import ai.maum.beframework.conf.security.encoder.DocumentPasswordEncoder;
import ai.maum.beframework.conf.security.encoder.JwtPasswordEncoder;
import ai.maum.beframework.conf.security.filter.AccessFilter;
import ai.maum.beframework.conf.security.filter.JwtFilter;
import ai.maum.beframework.model.repository.DocumentUserRepository;
import ai.maum.beframework.model.repository.UserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsConfigurationSource;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

import java.util.List;

/**
 * 보안 설정
 * @author baekgol@maum.ai
 */
@Configuration
@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
public class SecurityConfig {
    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http,
                                                            UserRepository userRepository,
                                                            JwtAuthenticationManager jwtAuthenticationManager,
                                                            DocumentAuthenticationManager documentAuthenticationManager,
                                                            AnonymousAuthenticationManager anonymousAuthenticationManager,
                                                            SecurityProperties securityProperties) {
        return http
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .authorizeExchange(exchanges -> exchanges
                        .pathMatchers(securityProperties.getExcludedPaths()).permitAll()
                        .anyExchange().authenticated())
                .formLogin(formLoginSpec -> formLoginSpec.authenticationManager(documentAuthenticationManager))
                .addFilterBefore(new AccessFilter(securityProperties.getExcludedPaths(), anonymousAuthenticationManager), SecurityWebFiltersOrder.AUTHENTICATION)
                .addFilterBefore(new JwtFilter(userRepository, jwtAuthenticationManager), SecurityWebFiltersOrder.AUTHENTICATION)
                .build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource(SecurityProperties securityProperties) {
        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        final CorsConfiguration config = new CorsConfiguration();

        for(int port=3000; port<=3100; port++)
            config.addAllowedOrigin("http://localhost:" + port);

        final List<String> origins = config.getAllowedOrigins();

        if(origins != null) origins.addAll(securityProperties.getAllowedOrigins());

        config.setAllowedOrigins(origins);
        config.addAllowedOrigin("null");
        config.setAllowedHeaders(securityProperties.getAllowedHeaders());
        config.setAllowedMethods(securityProperties.getAllowedMethods());
        config.setAllowCredentials(true);
        source.registerCorsConfiguration("/**", config);

        return source;
    }

    @Bean
    @Primary
    public JwtAuthenticationManager jwtAuthenticationManager(JwtPasswordEncoder passwordEncoder, UserRepository userRepository) {
        return new JwtAuthenticationManager(passwordEncoder, new JwtUserDetailsService(userRepository));
    }

    @Bean
    public DocumentAuthenticationManager documentAuthenticationManager(DocumentPasswordEncoder passwordEncoder, DocumentUserRepository documentUserRepository) {
        return new DocumentAuthenticationManager(passwordEncoder, new DocumentUserDetailsService(documentUserRepository));
    }

    @Bean
    public AnonymousAuthenticationManager anonymousAuthenticationManager(AnonymousPasswordEncoder passwordEncoder) {
        return new AnonymousAuthenticationManager(passwordEncoder, new AnonymousUserDetailsService());
    }

    @Bean
    @Primary
    public JwtPasswordEncoder jwtPasswordEncoder() {
        return new JwtPasswordEncoder();
    }

    @Bean
    public DocumentPasswordEncoder documentPasswordEncoder() {
        return new DocumentPasswordEncoder();
    }

    @Bean
    public AnonymousPasswordEncoder anonymousPasswordEncoder() {
        return new AnonymousPasswordEncoder();
    }
}
