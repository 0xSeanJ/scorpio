package top.jshanet.scorpio.framework.util;

import org.springframework.security.core.Authentication;
import top.jshanet.scorpio.framework.core.context.ScorpioContext;
import top.jshanet.scorpio.framework.core.context.ScorpioContextHolder;

/**
 * @author seanjiang
 * @date 2019/12/25
 */
public class ScorpioContextUtils {

    public static ScorpioContext getContext() {
        return ScorpioContextHolder.getContext();
    }

    public static void setContext(ScorpioContext context) {
        ScorpioContextHolder.setContext(context);
    }

    public static void unsetContext() {
        ScorpioContextHolder.removeContext();
    }

    public static String getRequestNo() {
        ScorpioContext context = ScorpioContextHolder.getContext();
        String requestNo = null;
        if (context != null) {
            requestNo = context.getRequestNo();
        }
        return requestNo;
    }

    public static void setRequestNo(String requestNo) {
        ScorpioContext context = ScorpioContextHolder.getContext();

        if (context == null) {
            context = new ScorpioContext();
            ScorpioContextHolder.setContext(context);
        }
        context.setRequestNo(requestNo);

    }

    public static void setAuthentication(Authentication authentication) {
        ScorpioContext context = ScorpioContextHolder.getContext();
        if (context == null) {
            context = new ScorpioContext();
            ScorpioContextHolder.setContext(context);
        }
    }

    public static Authentication getAuthentication() {
        ScorpioContext context = ScorpioContextHolder.getContext();
        Authentication authentication = null;
        if (context != null) {
            authentication = context.getAuthentication();
        }
        return authentication;
    }


}
