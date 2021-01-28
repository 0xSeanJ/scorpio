package top.jshanet.scorpio.framework.enumerate;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import top.jshanet.scorpio.framework.core.enumerate.EnumHandler;

import java.util.Map;

/**
 * @author seanjiang
 * @since 2020-07-13
 */
@SuppressWarnings({"rawtypes", "unchecked"})
public interface ScorpioEnum {

    @JsonValue
    default Map<String, Object> getEnumMap() {
        return EnumHandler.getEnumMap(this);
    }

    @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
    default ScorpioEnum construct(Object value) {
        if (value instanceof String) {
            Enum enumThis = (Enum) this;
            return (ScorpioEnum) Enum.valueOf(enumThis.getDeclaringClass(), value.toString());
        }
        return (ScorpioEnum) value;
    }
}
i