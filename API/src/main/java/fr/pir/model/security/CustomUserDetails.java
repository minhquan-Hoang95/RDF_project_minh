package fr.pir.model.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIgnore;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import fr.pir.model.user.User;

import lombok.Data;

@Data
public class CustomUserDetails implements UserDetails {

    private static final long serialVersionUID = 333L;

    private static final Logger L = LogManager.getLogger(CustomUserDetails.class);

    private String id;
    private String email;
    private boolean activate;
    @JsonIgnore
    private String password;
    private Collection<? extends GrantedAuthority> grantedAuthorities;

    public CustomUserDetails(User user, Collection<? extends GrantedAuthority> grantedAuthorities) {
        L.debug("id : {}", user.getId());

        this.id = String.valueOf(user.getId());
        this.email = user.getEmail();
        this.activate = user.isActivate();
        this.password = user.getPassword();
        this.grantedAuthorities = grantedAuthorities;
    }

    public static CustomUserDetails build(User user) {
        L.debug("id : {}", user.getId());

        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_" + user.getRole().getName()));

        return new CustomUserDetails(user, authorities);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        L.debug("");

        return this.grantedAuthorities;
    }

    @Override
    public String getUsername() {
        L.debug("");

        return this.id;
    }

    @Override
    public boolean isAccountNonExpired() {
        L.debug("");

        return this.activate;
    }

    @Override
    public boolean isAccountNonLocked() {
        L.debug("");

        return this.activate;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        L.debug("");

        return true;
    }

    @Override
    public boolean isEnabled() {
        L.debug("");

        return this.activate;
    }

    @Override
    public String getPassword() {
        L.debug("");

        return this.password;
    }

}
