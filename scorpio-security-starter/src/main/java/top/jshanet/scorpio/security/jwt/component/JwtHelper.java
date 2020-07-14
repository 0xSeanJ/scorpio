package top.jshanet.scorpio.security.jwt.component;

import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import top.jshanet.scorpio.security.autoconfig.properties.ScorpioSecurityProperties;
import top.jshanet.scorpio.security.jwt.domain.Jwt;
import top.jshanet.scorpio.security.jwt.domain.ScorpioUserDetails;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.stream.Collectors;

/**
 * @author jshanet
 * @since 2020-04-20
 */
public class JwtHelper {

    private static final SignatureAlgorithm SIGNATURE_ALGORITHM = SignatureAlgorithm.HS256;

    @Autowired
    private ScorpioSecurityProperties securityProperties;

    public Jwt generateToken(UserDetails userDetails) {
        Date expirationDate = generateExpirationDate();
        String token = Jwts.builder()
                .setIssuer(securityProperties.getJwt().getIssuer())
                .setSubject(userDetails.getUsername())
                .setAudience("WEB")
                .setIssuedAt(new Date())
                .setExpiration(expirationDate)
                .claim("roles",
                        userDetails.getAuthorities().stream()
                                .map(GrantedAuthority::getAuthority)
                                .collect(Collectors.toList()))
                .signWith(SIGNATURE_ALGORITHM, securityProperties.getJwt().getSecret())
                .compact();

        return new Jwt(token, expirationDate);
    }

    public String getAuthToken(HttpServletRequest request) {
        String authHeader = getAuthHeaderFromHeader(request);
        String tokenSchema = securityProperties.getJwt().getTokenSchema();
        if (authHeader != null && authHeader.startsWith(tokenSchema)) {
            return authHeader.substring(tokenSchema.length());
        }
        return null;
    }

    public UserDetails getUserDetailsFromToken(String token) throws JwtException {
        Claims claims = parseAuthToken(token);
        ScorpioUserDetails userDetails = new ScorpioUserDetails();
        userDetails.setUsername(claims.getSubject());
        userDetails.setPassword("");
        return userDetails;
    }

    public boolean validateToken(String token, UserDetails userDetails) throws ExpiredJwtException {
        return getUserDetailsFromToken(token).getUsername()
                .equals(userDetails.getUsername());
    }

    private Claims parseAuthToken(String token) throws JwtException {
        return Jwts.parser()
                .setSigningKey(securityProperties.getJwt().getSecret())
                .parseClaimsJws(token)
                .getBody();
    }

    private Date generateExpirationDate() {
        return new Date(new Date().getTime() + securityProperties.getJwt().getTokenValidityInSeconds() * 1000);
    }

    private String getAuthHeaderFromHeader(HttpServletRequest request) {
        return request.getHeader(securityProperties.getJwt().getTokenHeader());
    }

}
