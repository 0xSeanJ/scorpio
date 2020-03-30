package top.jshanet.scorpio.framework.common.context;

import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;

import java.util.Locale;

/**
 * @author seanjiang
 * @date 2019/12/25
 */
@Getter
@Setter
public class ScorpioContext implements SecurityContext {

    private String bizSeqNo;

    private Locale locale = Locale.CHINESE;

    private String tenantId;

    private Authentication authentication;

    @Override
    public void setAuthentication(Authentication authentication) {
        this.authentication = authentication;

    }


}
