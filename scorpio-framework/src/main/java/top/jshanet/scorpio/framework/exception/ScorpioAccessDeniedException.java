package top.jshanet.scorpio.framework.exception;

import org.springframework.security.access.AccessDeniedException;

public class ScorpioAccessDeniedException extends AccessDeniedException {
    public ScorpioAccessDeniedException(String msg) {
        super(msg);
    }

    public ScorpioAccessDeniedException(String msg, Throwable t) {
        super(msg, t);
    }
}
