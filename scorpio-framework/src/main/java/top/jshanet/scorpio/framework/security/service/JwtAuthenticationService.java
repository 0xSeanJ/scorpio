package top.jshanet.scorpio.framework.security.service;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import top.jshanet.scorpio.framework.exception.ScorpioException;
import top.jshanet.scorpio.framework.security.component.JwtTokenHelper;
import top.jshanet.scorpio.framework.security.domain.JwtAuthenticationToken;
import top.jshanet.scorpio.framework.security.domain.UserCredentials;

@Service
public class JwtAuthenticationService {

    private final AuthenticationManager authenticationManager;

    private final UserDetailsService userDetailsService;

    private final JwtTokenHelper tokenHelper;

    public JwtAuthenticationService(AuthenticationManager authenticationManager,
                                    UserDetailsService userDetailsService,
                                    JwtTokenHelper tokenHelper) {
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
        this.tokenHelper = tokenHelper;
    }

    public JwtAuthenticationToken authenticate(UserCredentials userCredentials) throws ScorpioException {
        try {
            final Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            userCredentials.getUsername(),
                            userCredentials.getPassword()
                    )
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);
            UserDetails userDetails = userDetailsService.loadUserByUsername(userCredentials.getUsername());
            return tokenHelper.generateToken(userDetails);
        } catch (UsernameNotFoundException e) {
            throw new ScorpioException(ScorpioStatus.USER_NOT_FOUND);
        } catch (BadCredentialsException e) {
            throw new ScorpioException(ScorpioStatus.INCORRECT_USER_CREDENTIAL);
        }

    }
}
