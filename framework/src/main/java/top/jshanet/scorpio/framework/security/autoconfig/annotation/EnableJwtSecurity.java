package top.jshanet.scorpio.framework.security.autoconfig.annotation;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import top.jshanet.scorpio.framework.security.autoconfig.JwtSecurityAutoConfiguration;
import top.jshanet.scorpio.framework.security.component.JwtAuthenticationEntryPoint;
import top.jshanet.scorpio.framework.security.component.JwtAuthenticationFilter;
import top.jshanet.scorpio.framework.security.component.JwtTokenHelper;
import top.jshanet.scorpio.framework.security.controller.JwtAuthenticationController;
import top.jshanet.scorpio.framework.security.service.JwtAuthenticationService;

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
        JwtTokenHelper.class,
        JwtSecurityAutoConfiguration.class,
        JwtAuthenticationService.class,
        JwtAuthenticationController.class
})
public @interface EnableJwtSecurity {

}
