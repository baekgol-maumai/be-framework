package ai.maum.beframework.util;

import ai.maum.beframework.vo.BaseException;
import ai.maum.beframework.codemessage.SystemCodeMsg;
import ai.maum.beframework.conf.properties.SecurityProperties;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.lang.Nullable;

import java.util.Date;
import java.util.Map;

/**
 * JWT 유틸
 * @author baekgol@maum.ai
 */
public class JwtUtil {
    private JwtUtil() {}

    public static String generate(Map<String, Object> claims, @Nullable String key) {
        return Jwts.builder()
                .setClaims(Jwts.claims(claims))
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + SecurityProperties.TOKEN_EXPIRE_TIME))
                .signWith(Keys.hmacShaKeyFor((key == null ? SecurityProperties.TOKEN_SECRET_KEY : key).getBytes()), SignatureAlgorithm.HS256)
                .compact();
    }

    public static Claims validateAndReturn(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(SecurityProperties.TOKEN_SECRET_KEY.getBytes())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch(ExpiredJwtException | SignatureException | MalformedJwtException e) {
            throw BaseException.of(SystemCodeMsg.UNAUTHORIZED);
        }
    }
}
