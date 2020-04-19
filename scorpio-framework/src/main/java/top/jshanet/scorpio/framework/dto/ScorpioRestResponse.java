package top.jshanet.scorpio.framework.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Page;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @author seanjiang
 * @date 2019/12/26
 */
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ScorpioRestResponse<T> extends ScorpioResponse {


    private Long totalCount;

    private Integer currentCount;

    private Integer totalPageCount;

    private T result;

    private List<T> data;

    public ScorpioRestResponse() {

    }

    public static <T> ScorpioRestResponse<T> from(T t) {
        ScorpioRestResponse<T> response = new ScorpioRestResponse<>();
        response.setResult(t);
        return response;
    }

    public static <T> ScorpioRestResponse<T> from(Page<T> tPage) {
        ScorpioRestResponse<T> response = new ScorpioRestResponse<>();
        response.setData(tPage.getContent());
        response.setTotalCount(tPage.getTotalElements());
        response.setCurrentCount(tPage.getSize());
        response.setTotalPageCount(tPage.getTotalPages());
        return response;
    }

    public static <T> ScorpioRestResponse<T> from(Set<T> tSet) {
        ScorpioRestResponse<T> response = new ScorpioRestResponse<>();
        response.setData(new ArrayList<>(tSet));
        response.setTotalCount((long) tSet.size());
        response.setCurrentCount(tSet.size());
        return response;
    }

    public static <T> ScorpioRestResponse<T> from(List<T> tList) {
        ScorpioRestResponse<T> response = new ScorpioRestResponse<>();
        response.setData(tList);
        response.setTotalCount((long) tList.size());
        response.setCurrentCount(tList.size());
        return response;
    }

}
