package top.jshanet.scorpio.security.autoconfig.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * @author jshanet
 * @since 2020-04-20
 */
@Setter
@Getter
public class JwtProperties {

    private boolean enable = false;

    private String tokenHeader = "Authorization";

    private String tokenSchema = "Bearer ";

    private String secret = UUID.randomUUID().toString();

    private String issuer = "app";

    private long tokenValidityInSeconds = 30 * 24 * 60 * 60;

    private boolean hideUserNotFoundExceptions = true;

    private Map<String, List<String>> permitAntMatchers = new HashMap<>();


}