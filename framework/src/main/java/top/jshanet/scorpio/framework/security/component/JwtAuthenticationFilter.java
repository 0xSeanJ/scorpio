package top.jshanet.scorpio.framework.security.component;

import io.jsonwebtoken.JwtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import top.jshanet.scorpio.framework.common.exception.ScorpioAuthenticationException;
import top.jshanet.scorpio.framework.security.domain.UserCredentials;

import javax.security.sasl.AuthenticationException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final UserDetailsService userDetailsService;
    private final JwtTokenHelper jwtTokenHelper;

    @Autowired
    public JwtAuthenticationFilter(UserDetailsService userDetailsService,
                                   JwtTokenHelper jwtTokenHelper) {
        this.userDetailsService = userDetailsService;
        this.jwtTokenHelper = jwtTokenHelper;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ScorpioAuthenticationException, ServletException, IOException {
        String token = jwtTokenHelper.getAuthToken(request);
        if (!StringUtils.isEmpty(token)) {

            restoreAuthentication(request, token);

        }
        filterChain.doFilter(request, response);
    }

    private void restoreAuthentication(HttpServletRequest request, String token) throws ScorpioAuthenticationException {
        try {
            UserCredentials credentials = jwtTokenHelper.getUserCredentialsFromToken(token);
            UserDetails userDetails = userDetailsService.loadUserByUsername(credentials.getUsername());
            if (jwtTokenHelper.validateToken(token, userDetails)) {
                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(
                                userDetails.getUsername(), token, userDetails.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (UsernameNotFoundException e) {
            throw new ScorpioAuthenticationException("username not found.", e);
        } catch (JwtException e) {
            throw new ScorpioAuthenticationException("JWT is not be trusted.", e);
        }
    }
}
