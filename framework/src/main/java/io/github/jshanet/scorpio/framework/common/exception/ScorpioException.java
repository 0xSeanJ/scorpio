package io.github.jshanet.scorpio.framework.common.exception;

import io.github.jshanet.scorpio.framework.common.constant.ScorpioStatus;

/**
 * @author seanjiang
 * @date 2019/12/25
 */
public class ScorpioException extends Exception {


    private ScorpioStatus status;

    public ScorpioException(ScorpioStatus status) {
        super(status.getMsg());
        this.status = status;
    }

    public ScorpioStatus getStatus() {
        return status;
    }

}
