package com.hcl.bankingrestapi.security.securitys;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@Slf4j
public class JwtTokenGenerator {

   // This APP_KEY segment is secret, only the server side knows
    @Value("${bankingrestapi.jwt.security.app.key}")
    private String APP_KEY;

    // Valid time of string jwt
    @Value("${bankingrestapi.jwt.security.expire.time}")
    private String EXPIRE_TIME;

    /**
     * Generate JWT from username, date, expiration date and secret used to Signature Algorithm ES512, API_KEY
     *
     * @param authentication
     * @return token
     */
    public String generateJwtToken(Authentication authentication) {
        JwtUserDetails jwtUserDetails = (JwtUserDetails) authentication.getPrincipal();
        Date expireDate = new Date(new Date().getTime() + EXPIRE_TIME);
        // Generate character json web token from id of user
        String token = Jwts.builder()
                .setSubject(Long.toString(jwtUserDetails.getId()))
                .setIssuedAt(new Date())
                .setExpiration(expireDate)
                .signWith(SignatureAlgorithm.ES512, APP_KEY)
                .compact();
        log.info("Generate Jwt Token successful: {}", token);
        return token;
    }

    /**
     * This function used to find used id by token
     *
     * @param token
     * @return userIdStr
     */
    public Long findUserIdByToken(String token) {
        Jws<Claims> claimsJws = parseToken(token);
        String userIdStr = claimsJws.getBody().getSubject();
        return Long.parseLong(userIdStr);

    }

    private Jws<Claims> parseToken(String token) {
        Jws<Claims> claimsJws = Jwts.parser()
                .setSigningKey(APP_KEY)
                .parseClaimsJws(token);
        return claimsJws;
    }

    /**
     * This function used to validate token
     *
     * @param token
     * @return isValid
     */
    public boolean validateToken(String token) {
        boolean isValid;
        try {
            Jws<Claims> claimsJws = parseToken(token);
            isValid = !isTokenExpired(claimsJws);
        } catch (Exception e) {
            isValid = false;
        }
        return isValid;
    }

    private boolean isTokenExpired(Jws<Claims> claimsJws) {
        Date expirationDate = claimsJws.getBody().getExpiration();
        return expirationDate.before(new Date());
    }
}
