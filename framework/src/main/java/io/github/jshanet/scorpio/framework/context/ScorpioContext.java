package io.github.jshanet.scorpio.framework.context;

import lombok.Getter;
import lombok.Setter;

import java.util.Locale;

/**
 * @author seanjiang
 * @date 2019/12/25
 */
@Getter
@Setter
public class ScorpioContext {

    private String bizSeqNo;

    private Locale locale = Locale.CHINESE;



}
