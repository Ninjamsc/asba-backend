package com.technoserv.db.service.security.impl;

import com.technoserv.db.model.security.User;
import com.technoserv.db.model.security.UserProfile;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by 90630 on 17.01.2017.
 */
public class UserDTO implements UserDetails {

    private User user;

    public UserDTO(User user) {
        this.user = user;
    }

    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();

        for (UserProfile userProfile : user.getUserProfiles()) {
            authorities.add(new SimpleGrantedAuthority("ROLE_" + userProfile.getType()));
        }
        return authorities;
    }

    public String getPassword() {
        return user.getPassword();
    }

    public String getUsername() {
        return user.getSsoId();
    }

    public boolean isAccountNonExpired() {
        return true;
    }

    public boolean isAccountNonLocked() {
        return true;
    }

    public boolean isCredentialsNonExpired() {
        return true;
    }

    public boolean isEnabled() {
        return true;
    }

    public boolean isAdmin() {
        boolean result = false;
        for (GrantedAuthority grantedAuthority : getAuthorities()) {

            if (grantedAuthority.getAuthority().equalsIgnoreCase("ROLE_ADMIN"))
                result = true;

            if (grantedAuthority.getAuthority().equalsIgnoreCase("ROLE_WRITE"))
                result = true;
        }
        return result;
    }

    public String getFirstName() {
        return user.getFirstName();
    }

    public String getLastName() {
        return user.getLastName();
    }

    public String getEmail() {
        return user.getEmail();
    }
}
