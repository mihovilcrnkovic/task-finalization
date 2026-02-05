package com.example.task_finalization.service;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {

    public String getTokenFromContext() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth instanceof JwtAuthenticationToken) {
            return ((JwtAuthenticationToken) auth).getToken().getTokenValue();
        }
        return null;
    }

}
