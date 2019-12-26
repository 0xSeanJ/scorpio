package io.github.jshanet.scorpio.framework.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.github.jshanet.scorpio.common.util.ScorpioContextUtil;
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




}
