//package com.zosh.config;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.http.HttpMethod;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.http.SessionCreationPolicy;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.security.web.SecurityFilterChain;
//import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
//import org.springframework.web.cors.CorsConfiguration;
//import org.springframework.web.cors.CorsConfigurationSource;
//
//import jakarta.servlet.http.HttpServletRequest;
//import java.util.Arrays;
//import java.util.Collections;
//
//@Configuration
//public class AppConfig {
//
//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
//            .and()
//            .authorizeHttpRequests(Authorize -> Authorize
//                .requestMatchers("/api/**").authenticated()
//                .anyRequest().permitAll()
//            )
//            .addFilterBefore(new JwtTokenValidator(), BasicAuthenticationFilter.class)
//            .csrf().disable()
//            .cors().configurationSource(corsConfigurationSource())
//            .and()
//            .httpBasic()
//            .and()
//            .formLogin();
//
//        return http.build();
//    }
//
//    @Bean
//    public PasswordEncoder passwordEncoder() {
//        return new BCryptPasswordEncoder();
//    }
//
//    private CorsConfigurationSource corsConfigurationSource() {
//        return request -> {
//            CorsConfiguration config = new CorsConfiguration();
//            config.setAllowedOrigins(Arrays.asList(
//                "http://localhost:3000", 
//                "http://localhost:4000",
//                "http://localhost:4200",
//                "https://shopwithzosh.vercel.app",
//                "https://ecommerce-angular-blue.vercel.app/"
//            ));
//            config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE"));
//            config.setAllowCredentials(true);
//            config.setAllowedHeaders(Collections.singletonList("*"));
//            config.setExposedHeaders(Arrays.asList("Authorization"));
//            config.setMaxAge(3600L);
//            return config;
//        };
//    }
//}
