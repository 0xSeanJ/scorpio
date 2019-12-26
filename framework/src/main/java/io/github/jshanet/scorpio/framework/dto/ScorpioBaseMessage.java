package io.github.jshanet.scorpio.framework.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.github.jshanet.scorpio.framework.constant.ScorpioStatus;
import io.github.jshanet.scorpio.framework.util.ScorpioContextUtil;
import lombok.Data;

/**
 * @author seanjiang
 * @date 2019/12/25
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ScorpioBaseMessage extends ScorpioBaseDTO {

    private String code;

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
