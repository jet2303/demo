package com.daily.demo.config.securityConfig;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.daily.demo.config.security.CustomOncePerRequestFilter;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

        @Bean
        public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
                http
                                .authorizeHttpRequests(authorize -> authorize
                                                .requestMatchers(new AntPathRequestMatcher("/**"),
                                                                new AntPathRequestMatcher("/api/authenticate"),
                                                                new AntPathRequestMatcher("/login"))
                                                .permitAll()
                                                .anyRequest().authenticated())
                                .formLogin(formLogin -> formLogin
                                                .loginPage("/login")
                                                .permitAll())
                                .addFilterBefore(customOncePerRequestFilter(),
                                                UsernamePasswordAuthenticationFilter.class)
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

        @Bean
        public PasswordEncoder passwordEncoder() {
                return new BCryptPasswordEncoder();
        }

        @Bean
        public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
                        throws Exception {
                return authenticationConfiguration.getAuthenticationManager();
        }
}
