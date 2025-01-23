package ai.maum.beframework.conf.security;

import ai.maum.beframework.model.repository.DocumentUserRepository;
import ai.maum.beframework.conf.properties.SecurityProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
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
                                                            ReactiveAuthenticationManager authenticationManager,
                                                            SecurityProperties securityProperties) {
        final String[] excludedPaths = securityProperties.getExcludedPaths();

        return http
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .authorizeExchange(exchanges -> exchanges
                        .pathMatchers(excludedPaths).permitAll()
                        .anyExchange().authenticated())
                .formLogin(Customizer.withDefaults())
                .addFilterAt(new JwtFilter(Arrays.asList(excludedPaths), authenticationManager), SecurityWebFiltersOrder.AUTHENTICATION)
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
    public ReactiveAuthenticationManager authenticationManager(PasswordEncoder passwordEncoder, DocumentUserRepository documentUserRepository) {
        return new BaseUserDetailsAuthenticationManager(passwordEncoder, new BaseUserDetailsService(documentUserRepository));
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
