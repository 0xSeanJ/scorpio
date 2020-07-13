package top.jshanet.scorpio.framework.enumerate;

import com.fasterxml.jackson.annotation.JsonValue;
import top.jshanet.scorpio.framework.core.enumerate.EnumHandler;

import java.util.Map;

/**
 * @author seanjiang
 * @since 2020-07-13
 */
public interface ScorpioEnum {

    @JsonValue
    default Map<String, Object> getEnumMap() {
        return EnumHandler.getEnumMap(this);
    }

}
