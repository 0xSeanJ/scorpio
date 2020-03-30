package top.jshanet.scorpio.framework.security.autoconfig;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.CorsUtils;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import top.jshanet.scorpio.framework.security.autoconfig.properties.JwtSecurityProperties;
import top.jshanet.scorpio.framework.security.component.JwtAccessDeniedHandler;
import top.jshanet.scorpio.framework.security.component.JwtAuthenticationEntryPoint;
import top.jshanet.scorpio.framework.security.component.JwtAuthenticationFilter;

import java.util.Collections;

@Configuration
@ConditionalOnClass({
        WebSecurityConfigurerAdapter.class,
        AuthenticationManager.class})
@EnableConfigurationProperties({JwtSecurityProperties.class})
public class JwtSecurityAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean(PasswordEncoder.class)
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    @ConditionalOnMissingBean(AuthenticationEntryPoint.class)
    public AuthenticationEntryPoint authenticationEntryPoint() {
        return new JwtAuthenticationEntryPoint();
    }

    @Bean
    @ConditionalOnMissingBean(AccessDeniedHandler.class)
    public AccessDeniedHandler accessDeniedHandler() {
        return new JwtAccessDeniedHandler();
    }

    @Configuration
    @Order(Integer.MIN_VALUE)
    protected static class JwtWebSecurity extends WebSecurityConfigurerAdapter {

        private final JwtAuthenticationFilter jwtAuthFilter;

        private final PasswordEncoder passwordEncoder;

        private final UserDetailsService userDetailsService;

        private final AuthenticationEntryPoint authenticationEntryPoint;

        private final AccessDeniedHandler accessDeniedHandler;


        public JwtWebSecurity(JwtAuthenticationFilter jwtAuthFilter,
                              PasswordEncoder passwordEncoder,
                              UserDetailsService userDetailsService,
                              AuthenticationEntryPoint authenticationEntryPoint, AccessDeniedHandler accessDeniedHandler) {
            this.jwtAuthFilter = jwtAuthFilter;
            this.passwordEncoder = passwordEncoder;
            this.userDetailsService = userDetailsService;
            this.authenticationEntryPoint = authenticationEntryPoint;
            this.accessDeniedHandler = accessDeniedHandler;
        }

        @Override
        protected void configure(AuthenticationManagerBuilder auth) throws Exception {
            auth
                    .userDetailsService(userDetailsService)
                    .passwordEncoder(passwordEncoder);
        }

        @Bean
        CorsConfigurationSource corsConfigurationSource() {
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
            http
                    .csrf().disable()
                    .cors()
                    .and()  // 这里不能用cors().disable(), 会把自定义的header拦截掉
                    .sessionManagement()
                    .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                    .and()
                    .authorizeRequests()
                    .requestMatchers(CorsUtils::isPreFlightRequest).permitAll()
                    .antMatchers("/auth").permitAll()
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
