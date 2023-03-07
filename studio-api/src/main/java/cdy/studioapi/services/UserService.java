package cdy.studioapi.services;

import cdy.studioapi.infrastructure.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsernameIncludePermissions(username);
    }

    public Integer getTokenVersion(String username) throws UsernameNotFoundException {
        return userRepository.findTokenVersionByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("hata"));
    }
}
