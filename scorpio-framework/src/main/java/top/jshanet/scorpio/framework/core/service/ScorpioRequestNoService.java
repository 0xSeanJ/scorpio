package top.jshanet.scorpio.framework.core.service;

import top.jshanet.scorpio.framework.service.RequestNoService;
import top.jshanet.scorpio.framework.util.SeqUtils;

/**
 * @author seanjiang
 * @since 2020-07-14
 */
public class ScorpioRequestNoService implements RequestNoService {


    @Override
    public String nextRequestNo() {
        return SeqUtils.nextValue();
    }
}
