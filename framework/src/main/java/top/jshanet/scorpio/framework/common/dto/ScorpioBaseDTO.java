package top.jshanet.scorpio.framework.common.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

/**
 * @author seanjiang
 * @date 2019/12/25
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ScorpioBaseDTO {

    @Override
    public String toString() {
        return new ReflectionToStringBuilder(this).toString();
    }
}
