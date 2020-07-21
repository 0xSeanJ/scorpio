package top.jshanet.scorpio.framework.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import top.jshanet.scorpio.framework.exception.ScorpioException;
import top.jshanet.scorpio.framework.status.ScorpioStatus;
import top.jshanet.scorpio.framework.util.JsonMapper;
import top.jshanet.scorpio.framework.util.ScorpioContextUtils;

/**
 * @author seanjiang
 * @date 2019/12/25
 */
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ScorpioResponse extends ScorpioDto {

    private static final JsonMapper JSON_MAPPER = JsonMapper.nonDefaultMapper();

    private String code;

    private String msg;

    private String debugMsg;

    private String requestNo = ScorpioContextUtils.getRequestNo();

    public ScorpioResponse() {
        setStatus(ScorpioStatus.DefaultStatus.OK);
    }

    public static ScorpioResponse fromException(ScorpioException e) {
        ScorpioResponse scorpioResponse = new ScorpioResponse();
        scorpioResponse.setStatus(e.getStatus());
        scorpioResponse.setDebugMsg(e.getDebugMsg());
        return scorpioResponse;
    }

    public static ScorpioResponse fromStatus(ScorpioStatus status) {
        ScorpioResponse scorpioResponse = new ScorpioResponse();
        scorpioResponse.setStatus(status);
        return scorpioResponse;
    }

    public static ScorpioResponse fromStatus(ScorpioStatus status, String debugMsg) {
        ScorpioResponse response = fromStatus(status);
        response.setDebugMsg(debugMsg);
        return response;
    }

    public void setStatus(ScorpioStatus status) {
        this.code = status.getCode();
        this.msg = status.getMsg();
    }


    @Override
    public String toString() {
        return JSON_MAPPER.toJson(this);
    }
}
