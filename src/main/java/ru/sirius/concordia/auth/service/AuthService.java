package ru.sirius.concordia.auth.service;

import ru.sirius.concordia.auth.model.dto.response.AuthResponseDTO;
import ru.sirius.concordia.auth.model.security.jwt.JwtProcessor;
import ru.sirius.concordia.auth.model.security.principal.UserPrincipal;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

@Service
@AllArgsConstructor
public class AuthService {
    private final JwtProcessor jwtProcessor;

    private final AuthenticationManager authenticationManager;

    public AuthResponseDTO attemptLogin(String nickname, String password) {
        var authentication = getAuthentication(nickname, password);

        SecurityContextHolder.getContext().setAuthentication(authentication);

        var principal = (UserPrincipal) authentication.getPrincipal();

        var token = generatedAccessTokenByUserPrincipal(principal);

        return AuthResponseDTO.builder()
                .accessToken(token)
                .build();
    }

    private Authentication getAuthentication(String nickname, String password) {
        return authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(nickname, password)
        );
    }

    private String generatedAccessTokenByUserPrincipal(UserPrincipal userPrincipal) {
        return jwtProcessor.encode(
                JwtProcessor.Request.builder()
                        .userId(userPrincipal.getUserId())
                        .nickname(userPrincipal.getUsername())
                        .authorities(
                                convertCollectionAuthsToListString(userPrincipal.getAuthorities())
                        )
                        .build()
        );
    }

    private List<String> convertCollectionAuthsToListString(
            Collection<? extends GrantedAuthority> authorities
    ) {
        return authorities.stream().map(GrantedAuthority::getAuthority).toList();
    }
}
