package top.jshanet.scorpio.security.jwt.autoconfig;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * @author Administrator
 * @since 2020-04-20
 */
@Setter
@Getter
@ConfigurationProperties(prefix="scorpio.security.jwt")
public class JwtSecurityProperties {

    private String tokenHeader = "Authorization";

    private String tokenSchema = "Bearer ";

    private String secret = UUID.randomUUID().toString();

    private String issuer = "app";

    private long tokenValidityInSeconds = 1800;

    private boolean hideUserNotFoundExceptions = true;

    private Map<String, List<String>> antMatchers;


}