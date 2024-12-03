package ru.sirius.concordia.auth.model.security.jwt;

import com.auth0.jwt.interfaces.DecodedJWT;
import ru.sirius.concordia.auth.model.security.principal.UserPrincipal;
import lombok.AllArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@AllArgsConstructor
public class JwtToPrincipalConverter {


    public UserPrincipal convertToUserPrincipal(DecodedJWT jwt) {
        return UserPrincipal.builder()
                .userId(Long.valueOf(jwt.getSubject()))
                .username(jwt.getClaim(JwtProcessor.NICKNAME_CLAIM).asString())
                .authorities(
                        convertListStringToListSimpleGrantedAuthority(
                                getClaimOrEmptyList(jwt, JwtProcessor.AUTHORITIES_CLAIM)
                        )
                )
                .build();
    }

    private List<String> getClaimOrEmptyList(DecodedJWT jwt, String claim) {
        if (jwt.getClaim(claim).isNull())  {
            return List.of();
        }

        return jwt.getClaim(claim).asList(String.class);
    }

    private List<SimpleGrantedAuthority> convertListStringToListSimpleGrantedAuthority(
            List<String> authorities
    ) {
        return authorities
                .stream()
                .map(SimpleGrantedAuthority::new)
                .toList();
    }
}
