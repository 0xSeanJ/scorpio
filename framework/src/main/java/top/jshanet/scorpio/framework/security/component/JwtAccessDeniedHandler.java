package top.jshanet.scorpio.framework.security.component;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import top.jshanet.scorpio.framework.common.constant.ScorpioStatus;
import top.jshanet.scorpio.framework.common.util.ServletUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JwtAccessDeniedHandler implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException exception) throws IOException, ServletException {
        ServletUtils.printMessage(response, ScorpioStatus.ACCESS_DENIED, exception);
    }
}
