package com.example.bookshop_app.security.jwt;

import com.example.bookshop_app.security.BookstoreUserDetails;
import com.example.bookshop_app.security.BookstoreUserDetailsService;
import com.example.bookshop_app.security.jwt.repo.JwtBlacklistRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;

@Component
public class JWTRequestFilter extends OncePerRequestFilter {

    private final BookstoreUserDetailsService bookstoreUserDetailsService;
    private final JWTUtil jwtUtil;
    public JwtBlacklistRepository jwtBlacklistRepository;

    private static final Logger logger = LoggerFactory.getLogger(JWTRequestFilter.class);

    public JWTRequestFilter(BookstoreUserDetailsService bookstoreUserDetailsService,
                            JWTUtil jwtUtil, JwtBlacklistRepository jwtBlacklistRepository) {
        this.bookstoreUserDetailsService = bookstoreUserDetailsService;
        this.jwtBlacklistRepository = jwtBlacklistRepository;
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
                                    FilterChain filterChain) throws ServletException, IOException {
        String token = null;
        Cookie[] cookies = httpServletRequest.getCookies();
        String authHeader = httpServletRequest.getHeader("authorization");
        if (Objects.nonNull(authHeader) && !authHeader.isEmpty()) {
            String bearerToken = authHeader.substring(7);
            JwtBlacklist expiredToken = jwtBlacklistRepository.findByTokenEquals(bearerToken);
            if (Objects.nonNull(expiredToken)) {
                logger.info("token {} expired", expiredToken.getToken());
            } else {
                checkToken(httpServletRequest, bearerToken);
            }
        }

        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("token")) {
                    token = cookie.getValue();
                    JwtBlacklist expiredToken = jwtBlacklistRepository.findByTokenEquals(token);
                    if (Objects.nonNull(expiredToken)) {
                        logger.info("token {} expired", token);
                    } else {
                        checkToken(httpServletRequest, token);
                    }
                }
            }
        }

        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }

    private void checkToken(HttpServletRequest httpServletRequest, String token) {
        String username;
        username = jwtUtil.extractUsername(token);
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            validateToken(httpServletRequest, token, username);
        }
    }

    private void validateToken(HttpServletRequest httpServletRequest, String token, String username) {
        BookstoreUserDetails userDetails = (BookstoreUserDetails) bookstoreUserDetailsService.loadUserByUsername(username);
        if (jwtUtil.validateToken(token, userDetails)) {
            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(
                            userDetails, null, userDetails.getAuthorities());

            authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(httpServletRequest));
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        }
    }

}
