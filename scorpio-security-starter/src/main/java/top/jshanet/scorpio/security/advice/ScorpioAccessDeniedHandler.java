package top.jshanet.scorpio.security.advice;

import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import top.jshanet.scorpio.framework.dto.ScorpioResponse;
import top.jshanet.scorpio.framework.util.JsonMapper;
import top.jshanet.scorpio.security.status.SecurityStatus;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @author seanjiang
 * @since 2020-07-21
 */
public class ScorpioAccessDeniedHandler implements AccessDeniedHandler {

    private final static JsonMapper JSON_MAPPER = JsonMapper.nonDefaultMapper();


    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        ScorpioResponse scorpioResponse = ScorpioResponse.fromStatus(SecurityStatus.ACCESS_DENIED, accessDeniedException.getLocalizedMessage());
        String json = JSON_MAPPER.toJson(scorpioResponse);
        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        try (PrintWriter printWriter = response.getWriter()) {
            printWriter.print(json);
        }
    }
}
