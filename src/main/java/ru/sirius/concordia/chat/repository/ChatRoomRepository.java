package ru.sirius.concordia.chat.repository;

import ru.sirius.concordia.chat.model.ChatRoom;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface ChatRoomRepository extends MongoRepository<ChatRoom, String> {
    Optional<ChatRoom> findBySenderIdAndRecipientId(
            Long senderId,
            Long recipientId
    );
}