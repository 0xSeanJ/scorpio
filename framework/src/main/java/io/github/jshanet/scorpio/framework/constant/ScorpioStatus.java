package io.github.jshanet.scorpio.framework.constant;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

/**
 * @author seanjiang
 * @date 2019/12/25
 */
public class ScorpioStatus {

    private String code;

    private String msg;

    protected ScorpioStatus() {

    }

    protected ScorpioStatus(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public static ScorpioStatus valueOf(String code, String msg) {
        return new ScorpioStatus(code, msg);
    }


    public String getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    @Override
    public String toString() {
        return new ReflectionToStringBuilder(this).toString();
    }

    public static final ScorpioStatus SUCCESS = valueOf("0", "success");
}
