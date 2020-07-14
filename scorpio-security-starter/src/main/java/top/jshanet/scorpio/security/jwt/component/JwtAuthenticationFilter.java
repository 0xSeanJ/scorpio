package top.jshanet.scorpio.security.jwt.component;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author jshanet
 * @since 2020-04-20
 */
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final UserDetailsService userDetailsService;
    private final JwtHelper jwtHelper;


    @Autowired
    public JwtAuthenticationFilter(UserDetailsService userDetailsService,
                                   JwtHelper jwtHelper) {
        this.userDetailsService = userDetailsService;
        this.jwtHelper = jwtHelper;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String token = jwtHelper.getAuthToken(request);
        if (!StringUtils.isEmpty(token)) {
            try {
                restoreAuthentication(request, token);
            } catch (Exception e) {

            }
        }
        filterChain.doFilter(request, response);
    }

    private void restoreAuthentication(HttpServletRequest request, String token) {
        try {
            String username = jwtHelper.getUserDetailsFromToken(token).getUsername();
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
            if (jwtHelper.validateToken(token, userDetails)) {
                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(
                                userDetails.getUsername(), token, userDetails.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (Exception ignored) {

        }
    }
}