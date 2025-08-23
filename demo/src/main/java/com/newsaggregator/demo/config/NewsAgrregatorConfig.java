//package com.newsaggregator.demo.config;
//
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.http.SessionCreationPolicy;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.security.web.SecurityFilterChain;
//import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
//
//@Configuration
//public class NewsAgrregatorConfig {
//
//    @Bean
//    public PasswordEncoder passwordEncoder() {
//        return new BCryptPasswordEncoder();
//    }
//
//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity){
//        try{
//            httpSecurity
//                    .csrf(csrf -> csrf.disable())
//                    .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
//                    .authorizeHttpRequests(authorizeRequests -> authorizeRequests
//                            .requestMatchers("/api/register", "/api/signin","/h2-console/**").permitAll()
//                            .anyRequest().authenticated())
//                    .headers(headers -> headers.frameOptions(frameOptions -> frameOptions.disable()))
////                    .formLogin(formLogin -> formLogin.defaultSuccessUrl("/api/hello", true).permitAll())
//                    .addFilterBefore(new AuthJwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
//
//            return httpSecurity.build();
//        } catch (Exception e) {
//            throw new RuntimeException("Error configuring security filter chain", e);
//        }
//    }
//
//
//
//}

package com.newsaggregator.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class NewsAgrregatorConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // Expose AuthenticationManager so your /signin can authenticate credentials
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity){
        try{
            httpSecurity
                    .csrf(csrf -> csrf.disable())
                    .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                    .authorizeHttpRequests(authorizeRequests -> authorizeRequests
                            .requestMatchers("/api/register", "/api/signin","/h2-console/**").permitAll()
                            .anyRequest().authenticated())
                    .headers(headers -> headers.frameOptions(frameOptions -> frameOptions.disable()))
                    .addFilterBefore(new AuthJwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);

            return httpSecurity.build();
        } catch (Exception e) {
            throw new RuntimeException("Error configuring security filter chain", e);
        }
    }

    @Bean
    public WebClient webClient() {
        return WebClient.builder().build();
    }
}

