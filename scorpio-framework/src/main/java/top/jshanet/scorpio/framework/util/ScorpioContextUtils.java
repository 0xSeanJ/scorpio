package top.jshanet.scorpio.framework.util;

import org.springframework.security.core.Authentication;
import top.jshanet.scorpio.framework.context.ScorpioContext;
import top.jshanet.scorpio.framework.context.ScorpioContextHolder;

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

    public static String getBizSeqNo() {
        ScorpioContext context = ScorpioContextHolder.getContext();
        String bizSeqNo = null;
        if (context != null) {
            bizSeqNo = context.getBizSeqNo();
        }
        return bizSeqNo;
    }

    public static void setBizSeqNo(String bizSeqNo) {
        ScorpioContext context = ScorpioContextHolder.getContext();

        if (context == null) {
            context = new ScorpioContext();
            ScorpioContextHolder.setContext(context);
        }
        context.setBizSeqNo(bizSeqNo);

    }

    public static String getTenantId() {
        ScorpioContext context = ScorpioContextHolder.getContext();
        String tenantId = null;
        if (context != null) {
            tenantId = context.getTenantId();
        }
        return tenantId;
    }

    public static void setTenantId(String tenantId) {
        ScorpioContext context = ScorpioContextHolder.getContext();

        if (context == null) {
            context = new ScorpioContext();
            ScorpioContextHolder.setContext(context);
        }
        context.setTenantId(tenantId);

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


}
