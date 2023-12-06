package dev.canverse.studio.api.features.authentication.jwt;

import dev.canverse.studio.api.features.user.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class JwtAuthConverter implements Converter<Jwt, AbstractAuthenticationToken> {
    private final UserRepository userRepository;

    @Override
    public AbstractAuthenticationToken convert(Jwt source) {
        var user = userRepository.findByUsernameIncludePermissions(source.getSubject());
        return UsernamePasswordAuthenticationToken.authenticated(user, null, user.getAuthorities());
    }
}
