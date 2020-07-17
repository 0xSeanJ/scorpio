package top.jshanet.scorpio.framework.status;

import top.jshanet.scorpio.framework.core.status.StatusHandler;

public interface ScorpioStatus {

    default String getMsg() {
        return StatusHandler.get(this).getMsg();
    }

    default String getCode() {
        return StatusHandler.get(this).getCode();
    }

    enum DefaultStatus implements ScorpioStatus {
        @Status(code = 0, msg = "success") OK,
        @Status(code = 1, msg = "unknown error") UNKNOWN_ERROR,
        @Status(code = 2, msg = "timeout") WEB_TIMEOUT,
        @Status(code = 4, msg = "bad argument") BAD_ARGUMENT,
    }

}
