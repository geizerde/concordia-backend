package ru.sirius.concordia.auth.model.security.http.filter;

import ru.sirius.concordia.auth.model.security.jwt.JwtProcessor;
import ru.sirius.concordia.auth.model.security.jwt.JwtToPrincipalConverter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import ru.sirius.concordia.auth.model.security.rule.UserAuthenticationToken;

import java.io.IOException;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class JwtAuthentication extends OncePerRequestFilter {
    private final JwtProcessor jwtProcessor;

    private final JwtToPrincipalConverter jwtToPrincipalConverter;

    private final int BEGIN_TOKEN_INDEX = 7;

    private final String BEARER_PREFIX = "Bearer ";

    private final String AUTHORIZATION_HEADER = "Authorization";

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {
        extractTokenFromRequest(request)
                .map(jwtProcessor::decode)
                .map(jwtToPrincipalConverter::convertToUserPrincipal)
                .map(UserAuthenticationToken::new)
                .ifPresent(
                        authentication ->
                                SecurityContextHolder
                                        .getContext()
                                        .setAuthentication(authentication)
                );

        filterChain.doFilter(request, response);
    }

    private Optional<String> extractTokenFromRequest(HttpServletRequest request) {
        var token = request.getHeader(AUTHORIZATION_HEADER);

        if (StringUtils.hasText(token) && token.startsWith(BEARER_PREFIX)) {
            return Optional.of(token.substring(BEGIN_TOKEN_INDEX));
        }

        return Optional.empty();
    }
}
