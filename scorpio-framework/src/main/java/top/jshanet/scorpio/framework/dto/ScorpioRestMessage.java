package top.jshanet.scorpio.framework.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Page;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * @author seanjiang
 * @date 2019/12/26
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
        ScorpioRestMessage<T> response = new ScorpioRestMessage<>();
        response.setData(t);
        return response;
    }

    public static <T> ScorpioRestMessage<T> from(Page tPage) {
        ScorpioRestMessage<T> response = new ScorpioRestMessage<>();
        response.setData((T) tPage.getContent());
        response.setTotalCount(tPage.getTotalElements());
        response.setPageNo(tPage.getPageable().getPageNumber() + 1);
        response.setPageSize(tPage.getPageable().getPageSize());
        response.setTotalPage(tPage.getTotalPages());
        return response;
    }

    public static <T extends Collection> ScorpioRestMessage<T> from(Set tSet) {
        ScorpioRestMessage<T> response = new ScorpioRestMessage<>();
        response.setData((T) tSet);
        response.setTotalCount((long) tSet.size());
        return response;
    }

    public static <T extends Collection> ScorpioRestMessage<T> from(List tList) {
        ScorpioRestMessage<T> response = new ScorpioRestMessage<>();
        response.setData((T) tList);
        response.setTotalCount((long) tList.size());
        return response;
    }

}
