package cdy.studioapi.config;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

@Component
public class JwtAuthConverter implements Converter<Jwt, AbstractAuthenticationToken> {

    @Override
    public AbstractAuthenticationToken convert(Jwt source) {
        var scope = source.getClaimAsStringList("scope")
                .stream().map(SimpleGrantedAuthority::new).toList();
        var id = Integer.parseInt(source.getClaimAsString("id"));
        var username = source.getSubject();
        var email = source.getClaimAsString("email");
        var timezone = source.getClaimAsString("timezone");

        return new Auth(new SecurityUser(id, username, email, timezone), scope);
    }
}
