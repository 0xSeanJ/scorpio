package top.jshanet.scorpio.security.jwt.domain;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Administrator
 * @since 2020-04-20
 */
@Getter
@Setter
public class UserCredential {

    private String username;

    private String password;

}
