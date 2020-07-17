package top.jshanet.scorpio.framework.util;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

/**
 * @author seanjiang
 * @since 2020-05-18
 */
public class PageRequestUtils {

    public static PageRequest create(int pageNo, int pageSize, Sort sort) {
        return PageRequest.of(pageNo - 1, pageSize, sort);
    }

    public static PageRequest createSortDescByCreatedTime(int pageNo, int pageSize) {
        return create(pageNo, pageSize, Sort.by(Sort.Order.desc("createdTime")));
    }
}
