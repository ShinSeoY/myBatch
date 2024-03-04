//package com.my.batch.security;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.fasterxml.jackson.databind.SerializationFeature;
//import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
//import com.my.batch.constant.ResultCode;
//import com.my.batch.exception.ErrorResponse;
//import com.my.batch.exception.error.InvalidTokenException;
//import jakarta.servlet.FilterChain;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.web.filter.OncePerRequestFilter;
//
//
//@RequiredArgsConstructor
//@Slf4j
//public class AuthenticationTokenFilter extends OncePerRequestFilter {
//
//    private final AuthenticationTokenProvider authenticationTokenProvider;
//    private final MyUserDetailsService myUserDetailsService;
//
//    @Override
//    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) {
//        try {
//            String headerValue = request.getHeader("Authorization");
//            if (headerValue != null) {
//                String jwtString = authenticationTokenProvider.findToken(headerValue);
//                if (jwtString != null) {
//                    String username = authenticationTokenProvider.getUsername(jwtString);
//                    UserDetails userDetails = myUserDetailsService.loadUserByUsername(username);
//                    UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
//                    SecurityContextHolder.getContext().setAuthentication(token);
//                }
//            }
//            filterChain.doFilter(request, response);
//        } catch (InvalidTokenException e) {
//            setErrorResponse(request, response, e, ResultCode.INVALID_TOKEN);
//        } catch (Exception e) {
//            setErrorResponse(request, response, e, ResultCode.UNCAUGHT);
//        }
//    }
//
//    // 필터 예외 핸들링
//    private void setErrorResponse(HttpServletRequest request, HttpServletResponse response, Exception e, ResultCode errorCode) {
//        response.setStatus(errorCode.getHttpStatus().value());
//        response.setContentType("application/json");
//        response.setCharacterEncoding("UTF-8");
//        try {
//            ErrorResponse errorResponse = ErrorResponse.of(errorCode, request.getRequestURI());
//            for (StackTraceElement element : e.getStackTrace()) {
//                log.error(element.toString());
//            }
//            ObjectMapper objectMapper = new ObjectMapper();
//            // 역직렬화 설정
//            objectMapper.registerModule(new JavaTimeModule());
//            objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
//
//            String json = objectMapper.writeValueAsString(errorResponse);
//            response.getWriter().write(json);
//        } catch (Exception exception) {
//            log.error(exception.toString());
//        }
//
//    }
//
//}
