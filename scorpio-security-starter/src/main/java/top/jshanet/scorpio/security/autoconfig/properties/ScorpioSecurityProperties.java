package top.jshanet.scorpio.security.autoconfig.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;


/**
 * @author seanjiang
 * @since 2020-04-23
 */
@Setter
@Getter
@ConfigurationProperties(prefix="scorpio.security")
public class ScorpioSecurityProperties {


}
