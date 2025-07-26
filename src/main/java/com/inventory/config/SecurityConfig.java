package com.inventory.config;

import com.inventory.constants.CommonConstants;
import com.inventory.security.AuthFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {
    private AuthFilter authFilter;

    @Autowired
    SecurityConfig(AuthFilter authFilter) {
        this.authFilter = authFilter;
    }

    // Publicly accessible endpoints (Swagger + Auth)
//    public static final String[] PUBLIC_ENDPOINTS = {
//            "/api/swagger-resources/**",
//            "/api/swagger-ui/**",
//            "/api/swagger-ui.html",
//            "/api/api-docs/**",
//            CommonConstants.ApiPaths.BASE_API_PATH_WITH_VERSION + "/auth/**"
//    };

    public static final String[] PUBLIC_ENDPOINTS = new String[]{"/swagger-resources/**", "/swagger-ui/**",
            "/swagger-ui.html", "/swagger-ui/index.html", "/favicon.ico", "/v3/api-docs/**", CommonConstants.ApiPaths.BASE_API_PATH_WITH_VERSION + "/auth/**"};


    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http.csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(
                        auth -> auth
                                .requestMatchers(PUBLIC_ENDPOINTS).permitAll()
                                .anyRequest().authenticated())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(authFilter, UsernamePasswordAuthenticationFilter.class)
                .cors(Customizer.withDefaults())
                .build();
    }

//    @Bean
//    public WebSecurityCustomizer webSecurityCustomizer() {
//        return (web) -> web.ignoring().requestMatchers(
//                "/api/api-docs/**",
//                "/api/swagger-ui/**",
//                "/api/swagger-ui.html",
//                "/api/swagger-resources/**",
//                "/.well-known/**",
//                CommonConstants.ApiPaths.BASE_API_PATH_WITH_VERSION + "/auth/**"
//        );
//    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
