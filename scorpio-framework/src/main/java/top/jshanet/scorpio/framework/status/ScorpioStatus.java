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
        @Status(code = 0, msg = "Ok") OK,
        @Status(code = 1, msg = "Unknown Error") UNKNOWN_ERROR,
        @Status(code = 2, msg = "Web Timeout") WEB_TIMEOUT,
        @Status(code = 3, msg = "Bad Request") BAD_REQUEST,
        @Status(code = 4, msg = "Bad Argument") BAD_ARGUMENT,
    }

}
