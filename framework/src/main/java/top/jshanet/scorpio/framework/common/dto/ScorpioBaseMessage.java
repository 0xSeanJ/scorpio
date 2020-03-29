package top.jshanet.scorpio.framework.common.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import top.jshanet.scorpio.framework.common.constant.ScorpioStatus;
import top.jshanet.scorpio.framework.common.util.ScorpioContextUtil;
import lombok.Data;

/**
 * @author seanjiang
 * @date 2019/12/25
 */
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ScorpioBaseMessage extends ScorpioBaseDTO {

    private int code;

    private String msg;

    private String debugMsg;

    private String bizSeqNo = ScorpioContextUtil.getBizSeqNo();

    public ScorpioBaseMessage() {
        this.code = ScorpioStatus.SUCCESS.getCode();
        this.msg = ScorpioStatus.SUCCESS.getMsg();
    }

    public ScorpioBaseMessage(ScorpioStatus status) {
        this.code = status.getCode();
        this.msg = status.getMsg();
    }

    public void setStatus(ScorpioStatus status) {
        this.code = status.getCode();
        this.msg = status.getMsg();
    }


}
