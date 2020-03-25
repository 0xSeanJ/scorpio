package top.jshanet.scorpio.framework.common.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import org.springframework.data.domain.Page;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

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

    public static <T> ScorpioRestMessage<T> from(T t) {
        ScorpioRestMessage<T> restMessage = new ScorpioRestMessage<>();
        restMessage.setResult(t);
        return restMessage;
    }

    public static <T> ScorpioRestMessage<T> from(Page<T> tPage) {
        ScorpioRestMessage<T> restMessage = new ScorpioRestMessage<>();
        restMessage.setData(tPage.getContent());
        restMessage.setCount(tPage.getTotalElements());
        return restMessage;
    }

    public static <T> ScorpioRestMessage<T> from(Set<T> tSet) {
        ScorpioRestMessage<T> restMessage = new ScorpioRestMessage<>();
        restMessage.setData(new ArrayList<>(tSet));
        restMessage.setCount((long) tSet.size());
        return restMessage;
    }

    public static  <T> ScorpioRestMessage<T> from(List<T> tList) {
        ScorpioRestMessage<T> restMessage = new ScorpioRestMessage<>();
        restMessage.setData(tList);
        restMessage.setCount((long) tList.size());
        return restMessage;
    }

}
