package ru.sirius.concordia.chat.model;

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
   private String chatId;
   private Long senderId;
   private Long recipientId;
   private String senderName;
   private String recipientName;
   private String content;
   private Date timestamp;
   private MessageStatus status;
}