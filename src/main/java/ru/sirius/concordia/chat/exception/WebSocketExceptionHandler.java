package ru.sirius.concordia.chat.exception;

import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.security.access.AccessDeniedException;
import ru.sirius.concordia.core.model.dto.response.FailResponseDTO;

import java.security.Principal;

@ControllerAdvice
public class WebSocketExceptionHandler {

    private final SimpMessagingTemplate messagingTemplate;

    public WebSocketExceptionHandler(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    @MessageExceptionHandler(AccessDeniedException.class)
    public void handleAccessDeniedException(
            AccessDeniedException e,
            Principal principal
    ) {
        messagingTemplate.convertAndSendToUser(
                principal.getName(),
                "/queue/errors",
                FailResponseDTO.builder()
                        .message(e.getMessage())
                        .build()
        );
    }

    @MessageExceptionHandler(Exception.class)
    public void handleGenericException(
            Exception e,
            Principal principal
    ) {
        messagingTemplate.convertAndSendToUser(
                principal.getName(),
                "/queue/errors",
                FailResponseDTO.builder()
                        .message(e.getMessage())
                        .build()
        );
    }
}
