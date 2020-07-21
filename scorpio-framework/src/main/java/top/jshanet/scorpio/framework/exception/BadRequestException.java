package top.jshanet.scorpio.framework.exception;

import top.jshanet.scorpio.framework.status.ScorpioStatus;

/**
 * @author seanjiang
 * @since 2020-07-19
 */
public class BadRequestException extends ScorpioException {


    public BadRequestException(String debugMsg) {
        super(ScorpioStatus.DefaultStatus.BAD_REQUEST, debugMsg);
    }

    public BadRequestException() {
        super(ScorpioStatus.DefaultStatus.BAD_REQUEST);
    }
}
