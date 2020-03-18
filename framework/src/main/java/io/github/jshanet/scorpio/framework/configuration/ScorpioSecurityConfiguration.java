package io.github.jshanet.scorpio.framework.configuration;

import io.github.jshanet.scorpio.framework.security.provider.BackDoorAuthenticationProvider;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
public class ScorpioSecurityConfiguration extends WebSecurityConfigurerAdapter {

    protected AuthenticationProvider getBackDoorAuthenticationProvider() {
        return new BackDoorAuthenticationProvider();
    }

}
