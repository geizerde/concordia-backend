package ru.sirius.concordia.chat.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document
public class ChatMessage {

   public final static String STATUS_FIELD_KEY = "status";

   public final static String SENDER_ID_FIELD_KEY = "senderId";

   public final static String RECIPIENT_ID_FIELD_KEY = "recipientId";

   @Id
   private String id;

   @JsonProperty("chat_id")
   private String chatId;

   @JsonProperty("sender_id")
   private Long senderId;

   @JsonProperty("recipient_id")
   private Long recipientId;

   @JsonProperty("sender_name")
   private String senderName;

   @JsonProperty("recipient_name")
   private String recipientName;

   private String content;

   private Date timestamp;

   private MessageStatus status;
}