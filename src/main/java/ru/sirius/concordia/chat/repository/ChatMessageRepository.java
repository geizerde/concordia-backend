package ru.sirius.concordia.chat.repository;

import ru.sirius.concordia.chat.model.ChatMessage;
import ru.sirius.concordia.chat.model.MessageStatus;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface ChatMessageRepository extends MongoRepository<ChatMessage, String> {

    Long countBySenderIdAndRecipientIdAndStatus(
            Long senderId, Long recipientId, MessageStatus status
    );

    List<ChatMessage> findByChatId(String chatId);

    Optional<ChatMessage> findTopByChatIdOrderByTimestampDesc(String chatId);
}