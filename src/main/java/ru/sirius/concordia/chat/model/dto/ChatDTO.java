package ru.sirius.concordia.chat.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.sirius.concordia.chat.model.ChatMessage;
import ru.sirius.concordia.user.model.dto.UserDTO;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChatDTO {
    private UserDTO user;

    @JsonProperty("last_message")
    private ChatMessage lastMessage;
}
