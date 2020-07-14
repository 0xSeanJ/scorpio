package top.jshanet.scorpio.framework.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ScorpioDto {

    @Override
    public String toString() {
        return new ReflectionToStringBuilder(this).toString();
    }
}
