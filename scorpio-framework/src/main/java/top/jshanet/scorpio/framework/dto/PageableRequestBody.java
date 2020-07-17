package top.jshanet.scorpio.framework.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class PageableRequestBody extends ScorpioDto {

    private int pageNo = 1;

    private int pageSize = 10;

}
