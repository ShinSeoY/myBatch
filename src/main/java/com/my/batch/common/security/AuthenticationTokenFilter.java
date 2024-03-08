package com.my.batch.common.security;

import com.my.batch.exception.error.InvalidTokenException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
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
            throw new InvalidTokenException();
        } catch (Exception e) {
            throw e;
        }
    }

}
