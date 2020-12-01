package com.example.oauthtutor.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.Map;

@RestController
public class IndexController {

    @GetMapping("/user")
    public Map<String, Object> user(@AuthenticationPrincipal OAuth2User principal) {
        String name = "name";
        if (principal instanceof DefaultOAuth2User)
            name = ((DefaultOAuth2User) principal).getName();

        return Collections.singletonMap("name", name);
    }

    @GetMapping("/error")
    public String error(HttpServletRequest req) {
        return "error";
    }
}
