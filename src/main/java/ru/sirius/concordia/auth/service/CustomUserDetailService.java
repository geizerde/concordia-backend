package ru.sirius.concordia.auth.service;

import ru.sirius.concordia.auth.model.security.principal.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomUserDetailService implements UserDetailsService {
    private final UserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        var user = userService.findByNickname(username);

        return UserPrincipal.builder()
                .userId(user.getId())
                .username(user.getNickname())
                .password(user.getPassword())
                .authorities(List.of(new SimpleGrantedAuthority(
                        String.valueOf(user.getRole().getCode())
                )))
                .build();
    }
}
