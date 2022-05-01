package com.example.bookshop_app.security.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.stereotype.Component;

@Component
public class AuthorizationEventListener {

    private static final Logger logger = LoggerFactory.getLogger(AuthorizationEventListener.class);

    @EventListener(value = {AuthenticationSuccessEvent.class})
    public void onApplicationEvent(ApplicationEvent event) {
        Authentication auth = ((AuthenticationSuccessEvent) event).getAuthentication();
        if (auth.getPrincipal() instanceof DefaultOAuth2User) {
            logger.info("Login success with OAuth protocol");
        }
    }
}
