package ru.sirius.concordia.auth.config;

import ru.sirius.concordia.auth.model.security.http.filter.JwtAuthentication;
import ru.sirius.concordia.auth.service.CustomUserDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfig {
    private final JwtAuthentication jwtAuthentication;
    private final CustomUserDetailService customUserDetailService;
    private final PasswordEncoder passwordEncoder;

    @Bean
    public SecurityFilterChain authorizationFilterChain(HttpSecurity http) throws Exception {
        http.addFilterBefore(jwtAuthentication, UsernamePasswordAuthenticationFilter.class);

        http
                .cors(AbstractHttpConfigurer::disable)
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement((sm) -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .securityMatcher("/**").
                authorizeHttpRequests(
                        (authorize) -> authorize
                                .requestMatchers("/register").permitAll()
                                .requestMatchers("/login").permitAll()
                                .requestMatchers("/api/admin/**").hasRole(
                                        String.valueOf(RoleCode.ADMIN)
                                )
                                .requestMatchers("/api/**").authenticated()
                );

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        var builder = http.getSharedObject(AuthenticationManagerBuilder.class);
        builder
                .userDetailsService(customUserDetailService)
                .passwordEncoder(passwordEncoder);
        return builder.build();
    }

    public enum RoleCode {
        ADMIN, USER;
    }
}
