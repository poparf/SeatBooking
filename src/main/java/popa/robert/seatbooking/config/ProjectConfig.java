package popa.robert.seatbooking.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.converter.BufferedImageHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import java.awt.image.BufferedImage;


@Configuration
public class ProjectConfig {

    @Value("${keySetURI}")
    private String keySetUri;

    @Bean
    HttpMessageConverter<BufferedImage> bufferedImageHttpMessageConverter() {
        return new BufferedImageHttpMessageConverter();
    }

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

                        .requestMatchers(HttpMethod.POST,"/api/movies").hasAuthority("ROLE_ADMIN")
                        .requestMatchers(HttpMethod.DELETE,"/api/movies/*").hasAuthority("ROLE_ADMIN")
                        .requestMatchers(HttpMethod.GET,"/api/movies").permitAll()

                        .requestMatchers(HttpMethod.POST,"/api/rooms").hasAuthority("ROLE_ADMIN")
                        .requestMatchers(HttpMethod.GET,"/api/rooms/*").permitAll()

                        .requestMatchers(HttpMethod.POST,"/api/events").hasAuthority("ROLE_ADMIN")
                        .requestMatchers(HttpMethod.GET,"/api/events/*").permitAll()

                        .requestMatchers(HttpMethod.POST,"/api/seats").hasAuthority("ROLE_ADMIN")
                        .requestMatchers(HttpMethod.PATCH, "/api/seats/**").hasAuthority("ROLE_ADMIN")
                        .requestMatchers(HttpMethod.GET,"/api/seats/**").permitAll()

                        .requestMatchers(HttpMethod.GET, "/api/tickets/*").hasAnyAuthority("ROLE_USER", "ROLE_ADMIN")
                        .requestMatchers(HttpMethod.POST, "/api/tickets").hasAnyAuthority("ROLE_USER", "ROLE_ADMIN")


                        .anyRequest().permitAll());
        return http.build();
    }
}
