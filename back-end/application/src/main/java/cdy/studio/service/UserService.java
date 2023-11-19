package cdy.studio.service;

import cdy.studio.infrastructure.repositories.UserRepository;
import cdy.studio.service.views.UserView;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    public Page<UserView> getAll(Pageable page) {
        return userRepository.findBy((root, query, criteriaBuilder) -> null, r -> r.project("userRoles.role.rolePermissions.permission").sortBy(page.getSort()).page(page).map(UserView::new));
    }
}
