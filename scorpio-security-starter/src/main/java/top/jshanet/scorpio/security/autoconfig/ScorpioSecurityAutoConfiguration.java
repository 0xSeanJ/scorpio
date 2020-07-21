package top.jshanet.scorpio.security.autoconfig;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.*;
import org.springframework.core.Ordered;
import org.springframework.core.type.AnnotatedTypeMetadata;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.CorsUtils;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import top.jshanet.scorpio.security.advice.ScorpioAccessDeniedHandler;
import top.jshanet.scorpio.security.advice.ScorpioAuthenticationEntryPoint;
import top.jshanet.scorpio.security.advice.SecurityControllerAdvice;
import top.jshanet.scorpio.security.autoconfig.properties.JwtProperties;
import top.jshanet.scorpio.security.autoconfig.properties.ScorpioSecurityProperties;
import top.jshanet.scorpio.security.jwt.component.JwtAuthenticationFilter;
import top.jshanet.scorpio.security.jwt.component.JwtAuthenticator;
import top.jshanet.scorpio.security.jwt.component.JwtHelper;
import top.jshanet.scorpio.security.jwt.domain.ScorpioUserDetails;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * @author jshanet
 * @since 2020-04-20
 */
@AutoConfigureOrder(Ordered.LOWEST_PRECEDENCE)
@Configuration
@ConditionalOnClass({
        WebSecurityConfigurerAdapter.class,
        AuthenticationManager.class})
@EnableConfigurationProperties({ScorpioSecurityProperties.class, JwtProperties.class})
@Import({SecurityControllerAdvice.class, ScorpioAccessDeniedHandler.class, ScorpioAuthenticationEntryPoint.class})
public class ScorpioSecurityAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean(PasswordEncoder.class)
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }



    @ConditionalOnProperty(prefix = "scorpio.security.jwt", name = "enable",
            havingValue = "true")
    @Configuration
    @EnableWebSecurity
    @Import({JwtAuthenticationFilter.class, JwtHelper.class, JwtAuthenticator.class})
    protected static class JwtWebSecurity extends WebSecurityConfigurerAdapter {

        @Autowired
        private JwtAuthenticationFilter jwtAuthFilter;

        @Autowired
        private PasswordEncoder passwordEncoder;

        @Autowired
        private UserDetailsService userDetailsService;

        @Autowired
        private ScorpioSecurityProperties properties;

        @Autowired
        private JwtProperties jwtProperties;

        @Autowired
        private ScorpioAccessDeniedHandler accessDeniedHandler;

        @Autowired
        private ScorpioAuthenticationEntryPoint authenticationEntryPoint;


        @Override
        protected void configure(AuthenticationManagerBuilder auth) {
            auth.authenticationProvider(authenticationProvider());
        }

        @Bean
        public DaoAuthenticationProvider authenticationProvider() {
            DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
            System.out.println(properties);
            provider.setHideUserNotFoundExceptions(jwtProperties.isHideUserNotFoundExceptions());
            provider.setUserDetailsService(userDetailsService);
            provider.setPasswordEncoder(passwordEncoder);
            return provider;
        }

        @Bean
        public CorsConfigurationSource corsConfigurationSource() {
            CorsConfiguration configuration = new CorsConfiguration();
            configuration.setAllowedHeaders(Collections.singletonList("*"));
            configuration.setAllowedOrigins(Collections.singletonList("*"));
            configuration.setAllowedMethods(Collections.singletonList("*"));
            configuration.addExposedHeader(HttpHeaders.LOCATION); // 暴露 Location header
            UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
            source.registerCorsConfiguration("/**", configuration);
            return source;
        }


        @Bean
        @Override
        public AuthenticationManager authenticationManagerBean() throws Exception {
            return super.authenticationManagerBean();
        }

        @Override
        protected void configure(HttpSecurity http) throws Exception {
            for (Map.Entry<String, List<String>> entry: jwtProperties.getPermitAntMatchers().entrySet()){
                String method = entry.getKey().toUpperCase();
                if (method.equals("ANY")) {
                    http.authorizeRequests().antMatchers(
                            entry.getValue().toArray(new String[entry.getValue().size()])).permitAll();
                } else {
                    http.authorizeRequests().antMatchers(HttpMethod.resolve(method),
                            entry.getValue().toArray(new String[entry.getValue().size()])
                    ).permitAll();
                }
            }
            http
                    .csrf().disable()
                    .cors()
                    .and()  // 这里不能用cors().disable(), 会把自定义的header拦截掉
                    .sessionManagement()
                    .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                    .and()
                    .authorizeRequests()
                    .requestMatchers(CorsUtils::isPreFlightRequest).permitAll()
                    .anyRequest().authenticated()
                    .and()
                    .exceptionHandling()
                    .authenticationEntryPoint(authenticationEntryPoint)
                    .accessDeniedHandler(accessDeniedHandler)
                    .and()
                    .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
        }

    }
}
