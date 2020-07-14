package top.jshanet.scorpio.security.jwt.component;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import top.jshanet.scorpio.security.jwt.domain.Jwt;

/**
 * @author seanjiang
 * @since 2020-07-14
 */
public class JwtAuthenticator {


    private AuthenticationManager authenticationManager;

    private UserDetailsService userDetailsService;

    private JwtHelper jwtHelper;

    @Autowired
    public void setAuthenticationManager(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @Autowired
    public void setUserDetailsService(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Autowired
    public void setJwtHelper(JwtHelper jwtHelper) {
        this.jwtHelper = jwtHelper;
    }

    public Jwt authenticate(String username, String password) {
        Authentication authentication = this.authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);
        return this.jwtHelper.generateToken(userDetails);
    }

}
