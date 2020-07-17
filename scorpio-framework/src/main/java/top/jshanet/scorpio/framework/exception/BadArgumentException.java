package top.jshanet.scorpio.framework.exception;

import top.jshanet.scorpio.framework.status.ScorpioStatus;

/**
 * @author seanjiang
 * @since 2020-07-18
 */
public class BadArgumentException extends ScorpioException {


    public BadArgumentException() {
        super(ScorpioStatus.DefaultStatus.BAD_ARGUMENT);
    }

    public BadArgumentException(String debugMsg) {
        super(ScorpioStatus.DefaultStatus.BAD_ARGUMENT, debugMsg);
    }

    public BadArgumentException(Throwable throwable) {
        super(ScorpioStatus.DefaultStatus.BAD_ARGUMENT, throwable.getLocalizedMessage());
    }
}
