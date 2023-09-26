package com.tcit.vms.vms.jwt;

import com.tcit.vms.vms.service.UserDetailsImpl;
import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;
import org.springframework.web.util.WebUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
@Component
public class JwtUtils {
    private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);
    @Value("${cit.app.jwtSecret}")
    private String jwtSecret;
    @Value("${cit.app.jwtExpirationMs}")
    private int jwtExpirationMs;
    @Value("${cit.app.jwtCookieName}")
    private String jwtCookie;
    public String getJwtFromCookies(HttpServletRequest request) {
        Cookie cookie = WebUtils.getCookie(request, jwtCookie);
        if (cookie != null) {
            return cookie.getValue();
        } else {
            return null;
        }
    }
    public ResponseCookie generateJwtCookie(UserDetailsImpl userPrincipal) {
        String jwt = doGenerateToken(new HashMap<>(), userPrincipal.getEmail());
        ResponseCookie cookie = ResponseCookie.from(jwtCookie, jwt).path("/api").maxAge(604800).httpOnly(true).build();
        return cookie;
    }
    public ResponseCookie getCleanJwtCookie() {
        ResponseCookie cookie = ResponseCookie.from(jwtCookie, null).path("/api").build();
        return cookie;
    }
    public String getEmailFromJwtToken(String token) {
        return Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody().getSubject();
    }
    public boolean validateJwtToken(String authToken) {
        try {
            Claims claims = Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(authToken).getBody();
            return true;
        } catch (SignatureException e) {
            logger.error("Invalid JWT signature: {}", e.getMessage());
        } catch (MalformedJwtException e) {
            logger.error("Invalid JWT token: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            logger.error("JWT token is expired: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            logger.error("JWT token is unsupported: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            logger.error("JWT claims string is empty: {}", e.getMessage());
        }
        return false;
    }
    private String doGenerateToken(Map<String, Object> claims, String subject) {
        return Jwts.builder().setClaims(claims).setSubject(subject).setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpirationMs * 1000))
                .signWith(SignatureAlgorithm.HS512, jwtSecret).compact();
    }
    public Boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        final Date current = new Date(System.currentTimeMillis());
        return expiration.after(current);
    }
    public Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }
    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }
    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody();
    }
}
