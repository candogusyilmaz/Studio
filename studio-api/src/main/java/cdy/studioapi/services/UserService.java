package cdy.studioapi.services;

import cdy.studioapi.config.SecurityUser;
import cdy.studioapi.infrastructure.jpa.UserJpaRepository;
import cdy.studioapi.models.User;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserService implements UserDetailsService {
    private final UserJpaRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        var user = userRepository.findByUsernameIncludePermissions(username);
        return new SecurityUser(user);
    }

    public User getByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Username not found"));
    }

    public User getById(Integer id) throws UsernameNotFoundException {
        return userRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("User id not found"));
    }

    public Integer getTokenVersion(String username) throws UsernameNotFoundException {
        return userRepository.findTokenVersionByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("hata"));
    }
}
