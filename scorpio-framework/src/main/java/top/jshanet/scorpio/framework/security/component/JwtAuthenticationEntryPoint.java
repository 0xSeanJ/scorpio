package top.jshanet.scorpio.framework.security.component;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import top.jshanet.scorpio.framework.util.ServletUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException exception)
            throws IOException {
        ServletUtils.printMessage(response, ScorpioStatus.AUTHENTICATION_FAIL, exception);
    }
}
