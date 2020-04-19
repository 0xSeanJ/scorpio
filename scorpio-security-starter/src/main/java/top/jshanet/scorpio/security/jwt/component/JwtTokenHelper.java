package top.jshanet.scorpio.security.jwt.component;

import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import top.jshanet.scorpio.security.jwt.autoconfig.JwtSecurityProperties;
import top.jshanet.scorpio.security.jwt.domain.JwtObject;
import top.jshanet.scorpio.security.jwt.domain.UserCredential;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.stream.Collectors;

/**
 * @author Administrator
 * @since 2020-04-20
 */
@Component
public class JwtTokenHelper {

    private static final SignatureAlgorithm SIGNATURE_ALGORITHM = SignatureAlgorithm.HS256;

    private final JwtSecurityProperties jwtSecurityProperties;

    @Autowired
    public JwtTokenHelper(JwtSecurityProperties jwtSecurityProperties) {
        this.jwtSecurityProperties = jwtSecurityProperties;
    }

    public JwtObject generateToken(UserDetails userDetails) {
        Date expirationDate = generateExpirationDate();
        String token = Jwts.builder()
                .setIssuer(jwtSecurityProperties.getIssuer())
                .setSubject(userDetails.getUsername())
                .setAudience("WEB")
                .setIssuedAt(new Date())
                .setExpiration(expirationDate)
                .claim("roles",
                        userDetails.getAuthorities().stream()
                                .map(GrantedAuthority::getAuthority)
                                .collect(Collectors.toList()))
                .signWith(SIGNATURE_ALGORITHM, jwtSecurityProperties.getSecret())
                .compact();

        return new JwtObject(token, expirationDate);
    }

    public String getAuthToken(HttpServletRequest request) {
        String authHeader = getAuthHeaderFromHeader(request);
        String tokenSchema = jwtSecurityProperties.getTokenSchema();
        if (authHeader != null && authHeader.startsWith(tokenSchema)) {
            return authHeader.substring(tokenSchema.length());
        }
        return null;
    }

    public UserCredential getUserCredentialsFromToken(String token) throws JwtException {
        Claims claims = parseAuthToken(token);
        UserCredential userCredentials = new UserCredential();
        userCredentials.setUsername(claims.getSubject());
        userCredentials.setPassword("");
        return userCredentials;
    }

    public boolean validateToken(String token, UserDetails userDetails) throws ExpiredJwtException {
        return getUserCredentialsFromToken(token).getUsername()
                .equals(userDetails.getUsername());
    }

    private Claims parseAuthToken(String token) throws JwtException {
        return Jwts.parser()
                .setSigningKey(jwtSecurityProperties.getSecret())
                .parseClaimsJws(token)
                .getBody();
    }

    private Date generateExpirationDate() {
        return new Date(new Date().getTime() + jwtSecurityProperties.getTokenValidityInSeconds() * 1000);
    }

    private String getAuthHeaderFromHeader(HttpServletRequest request) {
        return request.getHeader(jwtSecurityProperties.getTokenHeader());
    }

}
