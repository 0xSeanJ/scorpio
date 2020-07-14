package top.jshanet.scorpio.security.jwt.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * @author jshanet
 * @since 2020-04-20
 */
@Setter
@Getter
@AllArgsConstructor
public class Jwt {
    private String token;
    private Date expiresOn;
}
