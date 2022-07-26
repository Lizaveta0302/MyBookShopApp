package com.example.bookshop_app.security.jwt;

import com.example.bookshop_app.security.jwt.repo.JwtBlacklistRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.Objects;

@Service
public class MyLogoutHandler implements LogoutHandler {

    @Autowired
    public JwtBlacklistRepository jwtBlacklistRepository;

    @Override
    public void logout(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) {
        if (Objects.nonNull(httpServletRequest.getCookies())) {
            JwtBlacklist jwtBlacklist = new JwtBlacklist();
            jwtBlacklist.setToken(Arrays.stream(httpServletRequest.getCookies()).filter(c -> c.getName().equals("token"))
                    .findFirst().map(Cookie::getValue).orElse(null));
            if (jwtBlacklist.getToken() != null) {
                jwtBlacklistRepository.save(jwtBlacklist);
            }
        }
    }

    public void setJwtBlacklistRepository(JwtBlacklistRepository jwtBlacklistRepository) {
        this.jwtBlacklistRepository = jwtBlacklistRepository;
    }
}
