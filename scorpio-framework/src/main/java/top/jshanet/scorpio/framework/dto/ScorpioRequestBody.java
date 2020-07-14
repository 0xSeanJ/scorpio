package top.jshanet.scorpio.framework.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class ScorpioRequestBody extends ScorpioDto {

    private int pageNum;

    private int pageSize;

}
