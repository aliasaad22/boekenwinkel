
package be.ali.boekenwinkel;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http)
            throws Exception {

        http
                .csrf(csrf -> csrf.disable())

                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/books", "/books/**").permitAll()
                        .requestMatchers("/users", "/users/**").permitAll()
                        .requestMatchers("/reservations", "/reservations/**").permitAll()
                        .requestMatchers("/reviews", "/reviews/**").permitAll()
                        .anyRequest().authenticated()
                );


        return http.build();
    }

}
