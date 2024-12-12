package ru.sirius.concordia.chat.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChatNotification {
    private String id;

    @JsonProperty("sender_id")
    private Long senderId;

    @JsonProperty("sender_name")
    private String senderName;
}