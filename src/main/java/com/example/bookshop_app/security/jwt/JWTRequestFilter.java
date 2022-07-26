package com.example.bookshop_app.security.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.bookshop_app.security.BookstoreUserDetailsService;
import com.example.bookshop_app.security.jwt.repo.JwtBlacklistRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import javax.servlet.FilterChain;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.Objects;

@Component
public class JWTRequestFilter extends OncePerRequestFilter {

    @Autowired
    @Qualifier("handlerExceptionResolver")
    private HandlerExceptionResolver resolver;

    private final JWTUtil jwtUtil;
    private final BookstoreUserDetailsService bookstoreUserDetailsService;
    private final JwtBlacklistRepository jwtBlacklistRepository;

    private static final Logger log = LoggerFactory.getLogger(JWTRequestFilter.class);

    public JWTRequestFilter(BookstoreUserDetailsService bookstoreUserDetailsService,
                            JWTUtil jwtUtil, JwtBlacklistRepository jwtBlacklistRepository) {
        this.bookstoreUserDetailsService = bookstoreUserDetailsService;
        this.jwtBlacklistRepository = jwtBlacklistRepository;
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
                                    FilterChain filterChain) {
        try {
            String token;
            Cookie[] cookies = httpServletRequest.getCookies();
            String authHeader = httpServletRequest.getHeader("authorization");
            checkHeader(httpServletRequest, authHeader);
            if (cookies != null) {
                for (Cookie cookie : cookies) {
                    if (cookie.getName().equals("token")) {
                        token = cookie.getValue();
                        JwtBlacklist expiredToken = jwtBlacklistRepository.findByTokenEquals(token);
                        DecodedJWT jwt = JWT.decode(token);
                        if (Objects.nonNull(expiredToken) || jwt.getExpiresAt().before(new Date())) {
                            log.warn("token {} expired", token);
                        } else {
                            checkToken(httpServletRequest, token);
                        }
                    }
                }
            }
            filterChain.doFilter(httpServletRequest, httpServletResponse);
        } catch (Exception ex) {
            log.warn("Spring Security Filter Chain Exception:", ex);
            resolver.resolveException(httpServletRequest, httpServletResponse, null, ex);
        }
    }

    private void checkHeader(HttpServletRequest httpServletRequest, String authHeader) {
        if (Objects.nonNull(authHeader) && !authHeader.isEmpty()) {
            String bearerToken = authHeader.substring(7);
            JwtBlacklist expiredToken = jwtBlacklistRepository.findByTokenEquals(bearerToken);
            if (Objects.nonNull(expiredToken)) {
                log.info("token {} expired", expiredToken.getToken());
            } else {
                checkToken(httpServletRequest, bearerToken);
            }
        }
    }

    private void checkToken(HttpServletRequest httpServletRequest, String token) {
        String username;
        username = jwtUtil.extractUsername(token);
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            validateToken(httpServletRequest, token, username);
        }
    }

    private void validateToken(HttpServletRequest httpServletRequest, String token, String username) {
        UserDetails userDetails = bookstoreUserDetailsService.loadUserByUsername(username);
        if (Boolean.TRUE.equals(jwtUtil.validateToken(token, userDetails))) {
            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(
                            userDetails, null, userDetails.getAuthorities());

            authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(httpServletRequest));
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        }
    }

}