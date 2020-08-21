package top.jshanet.scorpio.framework.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Page;
import top.jshanet.scorpio.framework.status.ScorpioStatus;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * @author seanjiang
 * @since 2019/12/26
 */
@Getter
@Setter
@SuppressWarnings({"unchecked", "rawtypes"})
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ScorpioRestMessage<T> extends ScorpioResponse {



    private Long totalCount;

    private Integer totalPage;

    private Integer pageNo;

    private Integer pageSize;

    private T data;

    public ScorpioRestMessage() {

    }

    public static <T> ScorpioRestMessage<T> from(T t) {
        if (t instanceof Page) {
            return fromPage((Page) t);
        } else if (t instanceof Collection) {
            return fromCollection((Collection) t);
        } else {
            ScorpioRestMessage<T> response = new ScorpioRestMessage<>();
            response.setData(t);
            return response;
        }
    }

    public static <T> ScorpioRestMessage<T> fromStatus(ScorpioStatus status, T data) {
        ScorpioRestMessage response = from(data);
        response.setStatus(status);
        return response;
    }

    public static <T> ScorpioRestMessage<T> fromPage(Page tPage) {
        ScorpioRestMessage<T> response = new ScorpioRestMessage<>();
        response.setData((T) tPage.getContent());
        response.setTotalCount(tPage.getTotalElements());
        response.setPageNo(tPage.getPageable().getPageNumber() + 1);
        response.setPageSize(tPage.getPageable().getPageSize());
        response.setTotalPage(tPage.getTotalPages());
        return response;
    }

    public static <T> ScorpioRestMessage<T> fromCollection(Collection collection) {
        ScorpioRestMessage<T> response = new ScorpioRestMessage<>();
        response.setData((T) collection);
        response.setTotalCount((long) collection.size());
        return response;
    }

}
