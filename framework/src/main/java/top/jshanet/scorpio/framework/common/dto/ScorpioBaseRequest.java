package top.jshanet.scorpio.framework.common.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class ScorpioBaseRequest extends ScorpioBaseDTO {

    private int pageNum;

    private int pageSize;

}
