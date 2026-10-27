package be.ali.boekenwinkel;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.web.cors.CorsConfiguration;

import java.util.List;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http)
            throws Exception {

        http
                .cors(cors -> cors.configurationSource(request -> {
                    CorsConfiguration config = new CorsConfiguration();

                    config.setAllowedOrigins(
                            List.of("http://localhost:63342")
                    );

                    config.setAllowedMethods(
                            List.of("GET", "POST", "PUT", "DELETE", "OPTIONS")
                    );

                    config.setAllowedHeaders(List.of("*"));
                    config.setAllowCredentials(true);

                    return config;
                }))
                .csrf(csrf -> csrf.disable())
                .securityContext(context -> context
                        .securityContextRepository(
                                new HttpSessionSecurityContextRepository()
                        )
                )
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/books", "/books/**").permitAll()
                        .requestMatchers("/users").permitAll()
                        .requestMatchers("/users/login").permitAll()
                        .anyRequest().authenticated()
                );

        return http.build();
    }
}