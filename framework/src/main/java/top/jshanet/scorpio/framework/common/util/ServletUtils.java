package top.jshanet.scorpio.framework.common.util;

import org.springframework.http.MediaType;
import top.jshanet.scorpio.framework.common.constant.ScorpioStatus;
import top.jshanet.scorpio.framework.common.dto.ScorpioBaseMessage;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class ServletUtils {

    private static final JsonMapper JSON_MAPPER = JsonMapper.nonDefaultMapper();

    public static void printMessage(HttpServletResponse response, ScorpioStatus scorpioStatus, Exception e) throws IOException {
        ScorpioBaseMessage baseMessage = new ScorpioBaseMessage(scorpioStatus);
        baseMessage.setDebugMsg(e.getMessage());
        String jsonMessage = JSON_MAPPER.toJson(baseMessage);
        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        response.setStatus(HttpServletResponse.SC_OK);
        try (PrintWriter printWriter = response.getWriter()) {
            printWriter.print(jsonMessage);
        }
    }

}
