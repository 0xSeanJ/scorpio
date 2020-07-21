package top.jshanet.scorpio.security.status;

import top.jshanet.scorpio.framework.status.ScorpioStatus;
import top.jshanet.scorpio.framework.status.Status;

/**
 * @author seanjiang
 * @since 2020-07-21
 */
public enum  SecurityStatus implements ScorpioStatus {

    @Status(code = 10, msg = "Unauthorized Client") UNAUTHORIZED_CLIENT,
    @Status(code = 11, msg = "ACCESS_DENIED") ACCESS_DENIED


}
