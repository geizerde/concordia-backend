package ru.sirius.concordia.chat.service;

import lombok.AllArgsConstructor;
import ru.sirius.concordia.chat.exception.ResourceNotFoundException;
import ru.sirius.concordia.chat.model.ChatMessage;
import ru.sirius.concordia.chat.model.MessageStatus;
import ru.sirius.concordia.chat.repository.ChatMessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import ru.sirius.concordia.core.model.dto.data.CountDTO;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class ChatMessageService {
    private final ChatMessageRepository repository;

    private final ChatRoomService chatRoomService;

    private final MongoOperations mongoOperations;

    public ChatMessage save(ChatMessage chatMessage) {
        chatMessage.setStatus(MessageStatus.RECEIVED);
        repository.save(chatMessage);
        return chatMessage;
    }

    public CountDTO countNewMessages(
            Long senderId,
            Long recipientId
    ) {
        return CountDTO.builder()
                .count(
                        repository.countBySenderIdAndRecipientIdAndStatus(
                                senderId,
                                recipientId,
                                MessageStatus.RECEIVED
                        )
                ).build();
    }

    public List<ChatMessage> findChatMessages(
            Long senderId,
            Long recipientId
    ) {
        var chatId = chatRoomService.getChatId(
                senderId,
                recipientId,
                false
        );

        var messages =
                chatId.map(cId -> repository.findByChatId(cId)).orElse(new ArrayList<>());

        if(!messages.isEmpty()) {
            updateStatuses(
                    senderId,
                    recipientId,
                    MessageStatus.DELIVERED
            );
        }

        return messages;
    }

    public ChatMessage findById(String id) {
        return repository
                .findById(id)
                .map(chatMessage -> {
                    chatMessage.setStatus(MessageStatus.DELIVERED);
                    return repository.save(chatMessage);
                })
                .orElseThrow(() ->
                        new ResourceNotFoundException("can't find message (" + id + ")"));
    }

    public void updateStatuses(
            Long senderId,
            Long recipientId,
            MessageStatus status
    ) {
        Query query = new Query(
                Criteria
                        .where(ChatMessage.SENDER_ID_FIELD_KEY).is(senderId)
                        .and(ChatMessage.RECIPIENT_ID_FIELD_KEY).is(recipientId)
        );

        Update update = Update.update(
                ChatMessage.STATUS_FIELD_KEY,
                status
        );

        mongoOperations.updateMulti(
                query,
                update,
                ChatMessage.class
        );
    }

    public ChatMessage findLastMessageInChat(String chatId) {
        return repository.findTopByChatIdOrderByTimestampDesc(chatId)
                .orElse(null);
    }
}
