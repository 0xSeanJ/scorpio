package top.jshanet.scorpio.framework.util;

import org.springframework.security.core.Authentication;
import top.jshanet.scorpio.framework.core.context.ScorpioContext;
import top.jshanet.scorpio.framework.core.context.ScorpioContextHolder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author seanjiang
 * @since 2019/12/25
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
        context.setAuthentication(authentication);
    }

    public static Authentication getAuthentication() {
        ScorpioContext context = ScorpioContextHolder.getContext();
        Authentication authentication = null;
        if (context != null) {
            authentication = context.getAuthentication();
        }
        return authentication;
    }

    public static void setHttpServletRequest(HttpServletRequest httpServletRequest) {
        ScorpioContext context = ScorpioContextHolder.getContext();
        if (context == null) {
            context = new ScorpioContext();
            ScorpioContextHolder.setContext(context);
        }
        context.setHttpServletRequest(httpServletRequest);
    }

    public static HttpServletRequest getHttpServletRequest() {
        ScorpioContext context = ScorpioContextHolder.getContext();
        HttpServletRequest httpServletRequest = null;
        if (context != null) {
            httpServletRequest = context.getHttpServletRequest();
        }
        return httpServletRequest;
    }

    public static void setHttpServletResponse(HttpServletResponse httpServletResponse) {
        ScorpioContext context = ScorpioContextHolder.getContext();
        if (context == null) {
            context = new ScorpioContext();
            ScorpioContextHolder.setContext(context);
        }
        context.setHttpServletResponse(httpServletResponse);
    }

    public static HttpServletResponse getHttpServletResponse() {
        ScorpioContext context = ScorpioContextHolder.getContext();
        HttpServletResponse httpServletResponse = null;
        if (context != null) {
            httpServletResponse = context.getHttpServletResponse();
        }
        return httpServletResponse;
    }

    public static String getUsername() {
        return getAuthentication().getPrincipal().toString();
    }


}
