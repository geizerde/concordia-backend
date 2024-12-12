package ru.sirius.concordia.chat.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document
public class ChatRoom {
    @Id
    private String id;

    @JsonProperty("chat_id")
    private String chatId;

    @JsonProperty("sender_id")
    private Long senderId;

    @JsonProperty("recipient_name")
    private Long recipientId;
}
