package top.jshanet.scorpio.security.jwt.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import top.jshanet.scorpio.framework.entity.ScorpioEntity;

import javax.persistence.MappedSuperclass;
import java.util.Collection;

/**
 * @author seanjiang
 * @since 2020-07-14
 */
@MappedSuperclass
public class ScorpioUserDetails extends ScorpioEntity implements UserDetails {


    private String username;

    @JsonIgnore
    private String password;


    private boolean enabled;


    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }
}
