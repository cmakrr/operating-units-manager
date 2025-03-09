package com.rnpc.operatingunit.auth.configuration;

import com.rnpc.operatingunit.auth.filter.JwtFilter;
import com.rnpc.operatingunit.service.AppUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {
    private final AppUserService userService;
    private final PasswordEncoder passwordEncoder;
    private final JwtFilter jwtAuthenticationFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
                .cors(cors -> cors.configurationSource(request -> {
                    CorsConfiguration corsConfig = new CorsConfiguration();
                    corsConfig.setAllowedOrigins(List.of("*"));
                    corsConfig.setAllowedMethods(List.of("GET", "HEAD", "POST", "PUT", "DELETE", "OPTIONS"));
                    corsConfig.setAllowedHeaders(List.of("*"));
                    corsConfig.setExposedHeaders(List.of("*"));

                    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
                    source.registerCorsConfiguration("/**", corsConfig);

                    return corsConfig;
                })).authorizeHttpRequests(request -> request
                        .requestMatchers("api/v1/auth/login", "api/v1/auth/test").permitAll()
                        .requestMatchers("api/v1/auth/register-user")
                        .hasAnyAuthority("ADMIN", "GENERAL_MANAGER")
                        .requestMatchers("/api/v1/tracker/operatingRoom/**").hasAuthority("TRACKER")
                        .requestMatchers("/api/v1/operatingRoom/name").permitAll()
                        .requestMatchers("/api/v1/operatingRoom/**", "api/v1/report/**").authenticated()
                        .requestMatchers("/api/v1/operationPlan/load", "/api/v1/appUsers/**")
                        .hasAnyAuthority("ADMIN", "GENERAL_MANAGER")
                        .requestMatchers("/api/v1/operations/**").hasAnyAuthority(
                                "ADMIN", "GENERAL_MANAGER", "MANAGER", "PLAN_CREATOR")
                        .requestMatchers("/api/v1/admin/**")
                        .hasAuthority("ADMIN")
                        .anyRequest().authenticated())
                .sessionManagement(manager -> manager.sessionCreationPolicy(STATELESS))
                .authenticationProvider(authenticationProvider())
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userService);
        authProvider.setPasswordEncoder(passwordEncoder);

        return authProvider;
    }
}
