package io.github.jshanet.scorpio.framework.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.List;

/**
 * @author seanjiang
 * @date 2019/12/26
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ScorpioRestMessage<T> extends ScorpioBaseMessage {


    Long count;

    private T result;

    private List<T> data;

    public ScorpioRestMessage() {

    }

}
