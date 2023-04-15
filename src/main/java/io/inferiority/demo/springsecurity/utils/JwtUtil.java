package io.inferiority.demo.springsecurity.utils;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.io.IOException;
import java.security.Key;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
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

    public static String createJwt(Key priv, Object data, long duration) {
        Objects.requireNonNull(priv, "private key can't be null");
        long nowMillis = System.currentTimeMillis();
        long expMillis = nowMillis + duration;
        JwtBuilder builder = Jwts.builder()
                .setId(UUID.randomUUID().toString())
                .setSubject("CN=easysec")
                .setIssuer("easysec")
                .setIssuedAt(new Date(nowMillis))
                .setExpiration(new Date(expMillis))
                .addClaims(Collections.singletonMap(ADDITIONAL, data))
                .signWith(SignatureAlgorithm.RS256, priv);
        return builder.compact();
    }

    public static HashMap<String, Object> parseJwt(Key pub, String token){
        Objects.requireNonNull(pub, "public key can't be null");
        Objects.requireNonNull(token, "token can't be null");
        return Jwts.parser().setSigningKey(pub).parseClaimsJws(token).getBody().get(ADDITIONAL, HashMap.class);
    }

    public static class UserVoJsonSerialize extends JsonSerializer<SimpleGrantedAuthority> {
        @Override
        public void serialize(SimpleGrantedAuthority value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
            if (value == null) {
                gen.writeNull();
            } else {
                gen.writeString(value.getAuthority());
            }
        }

    }
    public static class UserVoJsonDeserialize extends JsonDeserializer<SimpleGrantedAuthority> {
        @Override
        public SimpleGrantedAuthority deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JacksonException {
            if (p.getValueAsString() == null) {
                return null;
            } else {
                return new SimpleGrantedAuthority(p.getValueAsString());
            }
        }

    }
}