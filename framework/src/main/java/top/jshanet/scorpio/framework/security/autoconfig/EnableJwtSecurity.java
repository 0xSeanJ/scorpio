package top.jshanet.scorpio.framework.security.autoconfig;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import top.jshanet.scorpio.framework.security.component.JwtAuthenticationEntryPoint;
import top.jshanet.scorpio.framework.security.component.JwtAuthenticationFilter;
import top.jshanet.scorpio.framework.security.service.JwtTokenService;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true, jsr250Enabled = true, prePostEnabled = true)
@Import({
        JwtAuthenticationFilter.class,
        JwtAuthenticationEntryPoint.class,
        JwtTokenService.class,
       JwtSecurityAutoConfiguration.class
})
public @interface EnableJwtSecurity {

}
