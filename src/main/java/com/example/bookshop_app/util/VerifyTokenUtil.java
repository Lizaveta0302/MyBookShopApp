package com.example.bookshop_app.util;

import com.example.bookshop_app.dto.form.UserProfileForm;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.ObjectWriter;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.function.Function;

@Component
public class VerifyTokenUtil {

    @Value("${verify.token.secret}")
    private String secret;

    public String generateToken(UserProfileForm profileForm) throws JsonProcessingException {
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        String json = ow.writeValueAsString(profileForm);
        return Jwts
                .builder()
                .setClaims(new HashMap<>())
                .setSubject(json)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10))
                .signWith(SignatureAlgorithm.HS256, secret).compact();
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
    }

    public UserProfileForm extractProfileForm(String token) throws JsonProcessingException {
        ObjectReader or = new ObjectMapper().reader().forType(UserProfileForm.class);
        String json = extractClaim(token, Claims::getSubject);
        return or.readValue(json);
    }

}
