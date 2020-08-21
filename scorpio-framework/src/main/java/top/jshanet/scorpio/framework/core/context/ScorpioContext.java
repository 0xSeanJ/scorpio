package top.jshanet.scorpio.framework.core.context;

import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Locale;

/**
 * @author seanjiang
 * @since  2019/12/25
 */
@Getter
@Setter
public class ScorpioContext implements SecurityContext {

    private String requestNo;

    private Authentication authentication;

    private HttpServletRequest httpServletRequest;

    private HttpServletResponse httpServletResponse;


    @Override
    public Authentication getAuthentication() {
        return authentication;
    }

    @Override
    public void setAuthentication(Authentication authentication) {
        this.authentication = authentication;
    }
}
