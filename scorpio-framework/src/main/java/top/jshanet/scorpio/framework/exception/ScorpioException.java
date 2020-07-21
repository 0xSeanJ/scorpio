package top.jshanet.scorpio.framework.exception;

import top.jshanet.scorpio.framework.status.ScorpioStatus;

/**
 * @author seanjiang
 * @date 2019/12/25
 */
public class ScorpioException extends RuntimeException {


    private ScorpioStatus status;

    private String debugMsg;

    private Object data;

    public ScorpioException(ScorpioStatus status, String debugMsg) {
        super(status.getMsg());
        this.status = status;
        this.debugMsg = debugMsg;
    }

    public ScorpioException(ScorpioStatus status) {
        super(status.getMsg());
        this.status = status;
    }

    public ScorpioException(ScorpioStatus status, Object data) {
        super(status.getMsg());
        this.status = status;
        this.data = data;
    }

    public ScorpioStatus getStatus() {
        return status;
    }

    public String getDebugMsg() {
        return debugMsg;
    }

    public Object getData() {
        return data;
    }
}
