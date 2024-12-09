package ru.sirius.concordia.chat.repository;

import ru.sirius.concordia.chat.model.ChatMessage;
import ru.sirius.concordia.chat.model.MessageStatus;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ChatMessageRepository extends MongoRepository<ChatMessage, String> {

    Long countBySenderIdAndRecipientIdAndStatus(
            String senderId,
            String recipientId,
            MessageStatus status
    );

    List<ChatMessage> findByChatId(String chatId);
}