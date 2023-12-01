package com.daily.demo.config.securityConfig;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

import com.daily.demo.config.security.CustomOncePerRequestFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

        @Bean
        public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
                http
                                .authorizeHttpRequests(authorize -> authorize
                                                .requestMatchers("/blog/**").permitAll()
                                                .anyRequest().authenticated())
                                .formLogin(formLogin -> formLogin
                                                .loginPage("/login")
                                                .permitAll())
                                .addFilterBefore(null, null)
                                .headers(header -> header
                                                .frameOptions(frameOption -> frameOption
                                                                .disable()))
                                .csrf(csrfCustomizer -> csrfCustomizer
                                                .disable())
                                .rememberMe(Customizer.withDefaults());

                return http.build();
        }

        @Bean
        public CustomOncePerRequestFilter customOncePerRequestFilter() {
                return new CustomOncePerRequestFilter();
        }
}
