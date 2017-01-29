package com.rsw.product.context;

import org.json.JSONObject;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.jwt.Jwt;
import org.springframework.security.jwt.JwtHelper;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;

/**
 * Created by DAlms on 1/26/17.
 */
public abstract class SecurityUtils {

    public static String currentUserAsToken() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !(auth.getDetails() instanceof OAuth2AuthenticationDetails)) {
            return null;
        }
        OAuth2AuthenticationDetails details = (OAuth2AuthenticationDetails) auth.getDetails();

        return details.getTokenValue();
    }

    public static Jwt currentUserAsJwt() {
        String token = currentUserAsToken();
        return JwtHelper.decode(token);
    }

    public static String currentUserFirstName() {
        Jwt jwt = currentUserAsJwt();
        String claims = jwt.getClaims();

        JSONObject obj = new JSONObject(claims);
        String firstName = obj.getString("firstname");
        return firstName;
    }
}
