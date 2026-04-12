package fr.pir.service.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import fr.pir.model.security.CustomUserDetails;
import fr.pir.model.user.User;
import fr.pir.service.user.UserService;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private static final Logger L = LogManager.getLogger(CustomUserDetailsService.class);

    @Autowired
    private UserService userService;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        L.debug("email : {}", email);

        try {
            User user = this.userService.getUserByEmail(email);

            return CustomUserDetails.build(user);
        } catch (Exception e) {
            throw new UsernameNotFoundException(e.getMessage());
        }
    }

    public UserDetails loadUserById(String id) throws UsernameNotFoundException {
        L.debug("id : {}", id);

        try {
            User user = this.userService.getUserById(id);

            return CustomUserDetails.build(user);
        } catch (Exception e) {
            throw new UsernameNotFoundException(e.getMessage());
        }
    }

}
