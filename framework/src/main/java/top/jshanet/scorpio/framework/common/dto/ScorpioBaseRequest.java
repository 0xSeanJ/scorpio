package top.jshanet.scorpio.framework.common.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ScorpioBaseRequest extends ScorpioBaseDTO {

    private int pageNum;

    private int pageSize;

}
