package ai.maum.beframework.conf.security;

import ai.maum.beframework.conf.properties.SecurityProperties;
import ai.maum.beframework.conf.security.auth.manager.DocumentUserDetailsAuthenticationManager;
import ai.maum.beframework.conf.security.auth.manager.MainUserDetailsAuthenticationManager;
import ai.maum.beframework.conf.security.auth.userdetails.DocumentUserDetailsService;
import ai.maum.beframework.conf.security.auth.userdetails.MainUserDetailsService;
import ai.maum.beframework.conf.security.encoder.DocumentPasswordEncoder;
import ai.maum.beframework.conf.security.encoder.MainPasswordEncoder;
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

import java.util.Arrays;
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
                                                            MainUserDetailsAuthenticationManager mainAuthenticationManager,
                                                            DocumentUserDetailsAuthenticationManager documentAuthenticationManager,
                                                            SecurityProperties securityProperties) {
        final String[] excludedPaths = securityProperties.getExcludedPaths();

        return http
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .authorizeExchange(exchanges -> exchanges
                        .pathMatchers(excludedPaths).permitAll()
                        .anyExchange().authenticated())
                .formLogin(formLoginSpec -> formLoginSpec.authenticationManager(documentAuthenticationManager))
                .addFilterAt(new JwtFilter(Arrays.asList(excludedPaths), userRepository, mainAuthenticationManager), SecurityWebFiltersOrder.AUTHENTICATION)
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
    public MainUserDetailsAuthenticationManager mainAuthenticationManager(MainPasswordEncoder passwordEncoder, UserRepository userRepository) {
        return new MainUserDetailsAuthenticationManager(passwordEncoder, new MainUserDetailsService(userRepository));
    }

    @Bean
    public DocumentUserDetailsAuthenticationManager documentAuthenticationManager(DocumentPasswordEncoder passwordEncoder, DocumentUserRepository documentUserRepository) {
        return new DocumentUserDetailsAuthenticationManager(passwordEncoder, new DocumentUserDetailsService(documentUserRepository));
    }

    @Bean
    @Primary
    public MainPasswordEncoder mainPasswordEncoder() {
        return new MainPasswordEncoder();
    }

    @Bean
    public DocumentPasswordEncoder documentPasswordEncoder() {
        return new DocumentPasswordEncoder();
    }
}
