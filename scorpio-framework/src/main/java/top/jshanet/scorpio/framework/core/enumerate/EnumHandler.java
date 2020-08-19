package top.jshanet.scorpio.framework.core.enumerate;

import top.jshanet.scorpio.framework.enumerate.Description;
import top.jshanet.scorpio.framework.enumerate.Properties;
import top.jshanet.scorpio.framework.enumerate.Property;
import top.jshanet.scorpio.framework.enumerate.ScorpioEnum;

import java.util.HashMap;
import java.util.Map;

/**
 * @author seanjiang
 * @since 2020-07-13
 */
public class EnumHandler {

    public static Map<String, Object> getEnumMap(Object object) {
        try {
            Enum<?> enumObject = (Enum<?>) object;
            Map<String, Object> enumMap = new HashMap<>();
            enumMap.put("ordinal", enumObject.ordinal());
            enumMap.put("name", enumObject.name());
            Description description = object.getClass().getField(enumObject.name()).getAnnotation(Description.class);
            if (description != null) {
                enumMap.put("description", description.value());
            }
            Properties properties = object.getClass().getField(enumObject.name()).getAnnotation(Properties.class);
            if (properties != null) {
                for (Property property : properties.properties()) {
                    enumMap.put(property.name(), property.value());
                }
            }
            return enumMap;
        } catch (NoSuchFieldException e) {
            throw new Error(e);
        }
    }


}
