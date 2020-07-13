package top.jshanet.scorpio.framework.core.status;

import top.jshanet.scorpio.framework.status.Status;
import top.jshanet.scorpio.framework.status.StatusPrefix;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class StatusHandler {

    private static final Map<Object, StatusObject> CODE_MAP = new ConcurrentHashMap<>();

    private static final String LEFT_PAD = "5";

    public static StatusObject get(Object object) {
        if (CODE_MAP.containsKey(object)) {
            return CODE_MAP.get(object);
        }
        try {
            Enum<?> codeEnum = (Enum<?>) object;
            StatusPrefix statusPrefix = object.getClass().getAnnotation(StatusPrefix.class);
            Status status = object.getClass().getField(codeEnum.name()).getAnnotation(Status.class);
            StatusObject statusObject = new StatusObject();
            int cod = status != null ? status.code() : codeEnum.ordinal();
            statusObject.setCode(statusPrefix != null ? String.format("%s%0"+LEFT_PAD+"d", statusPrefix.value(), cod)
                    : String.valueOf(cod));
            statusObject.setMsg(status != null && status.msg().length() > 0 ? status.msg() : codeEnum.name());
            CODE_MAP.put(object,statusObject);
            return statusObject;
        } catch (NoSuchFieldException e) {
            throw new Error(e);
        }
    }

}
