package com.my.batch.common.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.my.batch.constant.ResultCode;
import com.my.batch.exception.ErrorResponse;
import com.my.batch.exception.error.InvalidTokenException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;


@RequiredArgsConstructor
@Slf4j
public class AuthenticationTokenFilter extends OncePerRequestFilter {

    private final AuthenticationTokenProvider authenticationTokenProvider;
    private final MyUserDetailsService myUserDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            String headerValue = request.getHeader("Authorization");
            if (headerValue != null) {
                String jwtString = authenticationTokenProvider.findToken(headerValue);
                if (jwtString != null) {
                    String email = authenticationTokenProvider.getEmail(jwtString);
                    UserDetails userDetails = myUserDetailsService.loadUserByUsername(email);
                    UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
                    SecurityContextHolder.getContext().setAuthentication(token);
                }
            }
            filterChain.doFilter(request, response);
        } catch (InvalidTokenException e) {
            setErrorResponse(request, response, ResultCode.INVALID_TOKEN);
        } catch (Exception e) {
            setErrorResponse(request, response, ResultCode.UNAUTHORIZED);
        }
    }

    @Component
    public static class CustomAccessDeniedHandler implements AccessDeniedHandler {
        @Override
        public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
            ResultCode resultCode = ResultCode.UNAUTHORIZED;
            setErrorResponse(request, response, resultCode);
        }
    }

    @Component
    public static class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {
        @Override
        public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
            ResultCode resultCode = ResultCode.UNAUTHORIZED;
            setErrorResponse(request, response, resultCode);
        }
    }

    private static void setErrorResponse(HttpServletRequest request, HttpServletResponse response, ResultCode resultCode) {
        response.setStatus(resultCode.getHttpStatus().value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");
        try {
            ErrorResponse errorResponse = ErrorResponse.of(resultCode, request.getRequestURI());
            ObjectMapper objectMapper = new ObjectMapper();
            // 역직렬화 설정
            objectMapper.registerModule(new JavaTimeModule());
            objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);

            String json = objectMapper.writeValueAsString(errorResponse);
            response.getWriter().write(json);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

}
