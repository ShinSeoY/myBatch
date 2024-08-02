package com.my.batch.common.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final AuthenticationTokenProvider authenticationTokenProvider;
    private final MyUserDetailsService myUserDetailsService;
    private final AuthenticationTokenFilter.CustomAccessDeniedHandler accessDeniedHandler;
    private final AuthenticationTokenFilter.CustomAuthenticationEntryPoint authEntryPoint;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.httpBasic((request) -> request.disable())
                .csrf(AbstractHttpConfigurer::disable)
                .cors(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests((requests) -> requests
                        .requestMatchers("/api/member/signup").permitAll()
                        .requestMatchers("/api/member/login").permitAll()
                        .requestMatchers("/api/member/check-email/*").permitAll()
                        .requestMatchers("/api/member/certification-msg").permitAll()
                        .requestMatchers("/api/member/certification-msg/check").permitAll()
                        .requestMatchers("/job/*").permitAll()
                        .anyRequest()
                        .authenticated())
                .sessionManagement((requests) -> requests.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(new AuthenticationTokenFilter(authenticationTokenProvider, myUserDetailsService), UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling((exceptionConfig) ->
                        exceptionConfig.authenticationEntryPoint(authEntryPoint).accessDeniedHandler(accessDeniedHandler)
                );
        return http.build();
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
