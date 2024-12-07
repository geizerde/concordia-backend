package ru.sirius.concordia.chat.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import ru.sirius.concordia.auth.model.security.rule.UserAuthenticationToken;
import ru.sirius.concordia.chat.model.ChatMessage;
import ru.sirius.concordia.chat.model.ChatNotification;
import ru.sirius.concordia.chat.service.ChatMessageService;
import ru.sirius.concordia.chat.service.ChatRoomService;
import ru.sirius.concordia.core.model.dto.data.CountDTO;
import ru.sirius.concordia.core.model.dto.response.FailResponseDTO;
import ru.sirius.concordia.core.model.dto.response.ResponseDTOInterface;
import ru.sirius.concordia.core.model.dto.response.SuccessResponseDTO;

import java.security.Principal;
import java.util.List;

@Controller
public class ChatController {

    @Autowired private SimpMessagingTemplate messagingTemplate;
    @Autowired private ChatMessageService chatMessageService;
    @Autowired private ChatRoomService chatRoomService;

    @MessageMapping("/chat")
    public void processMessage(
            @Payload ChatMessage chatMessage,
            Principal principal
    ) {
        if (
                !chatMessage.getSenderId().equals(
                        ((UserAuthenticationToken) principal)
                                .getPrincipal()
                                .getUserId()
                                .toString()
                )
        ) {
            throw new AccessDeniedException("You are not authorized to send messages to this recipient.");
        }

        var chatId = chatRoomService.getChatId(
                chatMessage.getSenderId(),
                chatMessage.getRecipientId(),
                true
        );

        chatMessage.setChatId(chatId.get());

        ChatMessage saved = chatMessageService.save(chatMessage);

        messagingTemplate.convertAndSendToUser(
                chatMessage.getRecipientId(),
                "/queue/messages",
                new ChatNotification(
                        saved.getId(),
                        saved.getSenderId(),
                        saved.getSenderName()
                )
        );
    }

    @GetMapping("/messages/{senderId}/{recipientId}/count")
    public ResponseEntity<ResponseDTOInterface> countNewMessages(
            @PathVariable String senderId,
            @PathVariable String recipientId
    ) {
        try {
            return ResponseEntity.ok(
                    SuccessResponseDTO.<CountDTO>builder()
                            .data(
                                    chatMessageService.countNewMessages(
                                            senderId,
                                            recipientId
                                    )
                            )
                            .build()
            );
        } catch (Exception e) {
            return new ResponseEntity<>(
                    FailResponseDTO.builder()
                            .message(e.getMessage())
                            .build(),
                    HttpStatus.FORBIDDEN
            );
        }
    }

    @GetMapping("/messages/{senderId}/{recipientId}")
    public ResponseEntity<ResponseDTOInterface> findChatMessages (
            @PathVariable String senderId,
            @PathVariable String recipientId
    ) {
        try {
            return ResponseEntity.ok(
                    SuccessResponseDTO.<List<ChatMessage>>builder()
                            .data(
                                    chatMessageService.findChatMessages(
                                            senderId,
                                            recipientId
                                    )
                            )
                            .build()
            );
        } catch (Exception e) {
            return new ResponseEntity<>(
                    FailResponseDTO.builder()
                            .message(e.getMessage())
                            .build(),
                    HttpStatus.FORBIDDEN
            );
        }
    }

    @GetMapping("/messages/{id}")
    public ResponseEntity<ResponseDTOInterface> findMessage (
            @PathVariable String id
    ) {
        try {
            return ResponseEntity.ok(
                    SuccessResponseDTO.<ChatMessage>builder()
                            .data(chatMessageService.findById(id))
                            .build()
            );
        } catch (Exception e) {
            return new ResponseEntity<>(
                    FailResponseDTO.builder()
                            .message(e.getMessage())
                            .build(),
                    HttpStatus.FORBIDDEN
            );
        }
    }
}