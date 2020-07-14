package top.jshanet.scorpio.framework.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import top.jshanet.scorpio.framework.status.ScorpioStatus;
import top.jshanet.scorpio.framework.util.ScorpioContextUtils;

/**
 * @author seanjiang
 * @date 2019/12/25
 */
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ScorpioResponse extends ScorpioDto {

    private String code;

    private String msg;

    private String debugMsg;

    private String requestNo = ScorpioContextUtils.getRequestNo();

    public ScorpioResponse() {
        setStatus(ScorpioStatus.DefaultStatus.SUCCESS);
    }

    public ScorpioResponse(ScorpioStatus status) {
        this.code = status.getCode();
        this.msg = status.getMsg();
    }

    public void setStatus(ScorpioStatus status) {
        this.code = status.getCode();
        this.msg = status.getMsg();
    }


}
