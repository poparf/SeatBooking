package popa.robert.seatbooking.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;


@Configuration
public class ProjectConfig {

    @Value("${keySetURI}")
    private String keySetUri;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
            http.csrf(csrf-> csrf.disable())
                    .oauth2ResourceServer(c ->
                            c.jwt(j ->
                                    j.jwkSetUri(keySetUri)))
                .authorizeHttpRequests(auth -> auth
                                .requestMatchers("/api/movies/**").hasAuthority("ROLE_ADMIN")
                                .requestMatchers("/api/rooms/**").hasAuthority("ROLE_ADMIN")
                                .requestMatchers("/api/events/**").hasAuthority("ROLE_ADMIN")
                                .requestMatchers("/api/seats/**").hasAuthority("ROLE_ADMIN")
                        .anyRequest().permitAll());
        return http.build();
    }
}
