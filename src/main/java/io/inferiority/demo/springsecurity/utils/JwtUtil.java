package io.inferiority.demo.springsecurity.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.apache.commons.lang3.StringUtils;

import java.security.Key;
import java.util.Date;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

/**
 * @auther: cuijiufeng
 * @date: 2023/4/15 10:33
 */
public class JwtUtil {
    public static final String ADDITIONAL = "additional";
    private JwtUtil() {
    }

    public static String createJwt(Key priv, Map<String, Object> data, long duration) {
        Objects.requireNonNull(priv, "private key can't be null");
        long nowMillis = System.currentTimeMillis();
        long expMillis = nowMillis + duration;
        JwtBuilder builder = Jwts.builder()
                .setId(UUID.randomUUID().toString())
                .setSubject("CN=easysec")
                .setIssuer("easysec")
                .setIssuedAt(new Date(nowMillis))
                .setExpiration(new Date(expMillis))
                .addClaims(data)
                .signWith(SignatureAlgorithm.RS256, priv);
        return builder.compact();
    }

    public static Claims parseJwt(Key pub, String token){
        Objects.requireNonNull(pub, "public key can't be null");
        if (StringUtils.isBlank(token)) {
            throw new NullPointerException("the token can not be null");
        }
        return Jwts.parser().setSigningKey(pub).parseClaimsJws(token).getBody();
    }
}