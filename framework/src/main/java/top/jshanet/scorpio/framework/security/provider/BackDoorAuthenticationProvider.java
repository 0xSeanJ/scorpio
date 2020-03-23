package top.jshanet.scorpio.framework.security.provider;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;

public class BackDoorAuthenticationProvider implements AuthenticationProvider {

    private String backDoorUsername = "scorpio";

    public void setBackDoorUsername(String backDoorUsername) {
        this.backDoorUsername = backDoorUsername;
    }

    public String getBackDoorUsername() {
        return backDoorUsername;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String name = authentication.getName();
        String password = authentication.getCredentials().toString();

        if (name.equals(getBackDoorUsername())) {
            Collection<GrantedAuthority> authorities = new ArrayList<>();
            authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
            authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
            return new UsernamePasswordAuthenticationToken(authentication.getPrincipal(), password, authorities);
        } else {
            return null;
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}
