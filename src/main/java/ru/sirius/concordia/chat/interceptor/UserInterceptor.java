package ru.sirius.concordia.chat.interceptor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import ru.sirius.concordia.auth.model.security.jwt.JwtProcessor;
import ru.sirius.concordia.auth.model.security.jwt.JwtToPrincipalConverter;
import ru.sirius.concordia.auth.model.security.rule.UserAuthenticationToken;

import java.util.List;
import java.util.Optional;

@Component
public class UserInterceptor implements ChannelInterceptor {

    @Autowired
    private JwtProcessor jwtProcessor;

    @Autowired
    private JwtToPrincipalConverter jwtToPrincipalConverter;

    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String BEARER_PREFIX = "Bearer ";
    private static final int BEGIN_TOKEN_INDEX = 7;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);

        if (StompCommand.CONNECT.equals(accessor.getCommand())) {
            extractTokenFromHeaders(accessor)
                    .map(jwtProcessor::decode)
                    .map(jwtToPrincipalConverter::convertToUserPrincipal)
                    .map(UserAuthenticationToken::new)
                    .ifPresent(authentication -> {
                        SecurityContextHolder.getContext().setAuthentication(authentication);
                        accessor.setUser(authentication);
                    });
        }

        return message;
    }

    private Optional<String> extractTokenFromHeaders(StompHeaderAccessor accessor) {
        List<String> authorizationHeaders = accessor.getNativeHeader(AUTHORIZATION_HEADER);
        if (authorizationHeaders != null && !authorizationHeaders.isEmpty()) {
            String token = authorizationHeaders.getFirst();
            if (StringUtils.hasText(token) && token.startsWith(BEARER_PREFIX)) {
                return Optional.of(token.substring(BEGIN_TOKEN_INDEX));
            }
        }
        return Optional.empty();
    }
}
