package cdy.studioapi.services;

import cdy.studioapi.config.SecurityUser;
import cdy.studioapi.dtos.UserView;
import cdy.studioapi.infrastructure.UserRepository;
import cdy.studioapi.models.User;
import com.blazebit.persistence.CriteriaBuilderFactory;
import com.blazebit.persistence.view.EntityViewManager;
import com.blazebit.persistence.view.EntityViewSetting;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityManager;

@Service
@AllArgsConstructor
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final CriteriaBuilderFactory cbf;
    private final EntityManager em;
    private final EntityViewManager evm;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        var cb = cbf.create(em, User.class)
                .where("username")
                .eq(username);

        var user = evm.applySetting(EntityViewSetting.create(UserView.class), cb)
                .getSingleResult();

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
