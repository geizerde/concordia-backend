package ru.sirius.concordia.chat.service;

import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import ru.sirius.concordia.chat.model.ChatMessage;
import ru.sirius.concordia.chat.model.dto.ChatDTO;
import ru.sirius.concordia.match.model.Match;
import ru.sirius.concordia.match.service.MatchService;
import ru.sirius.concordia.user.model.dto.UserDTO;

import java.util.*;

@Service
@AllArgsConstructor
public class ChatService {
    private final MatchService matchService;

    private final ChatRoomService chatRoomService;

    private final ChatMessageService chatMessageService;

    private final ModelMapper modelMapper;

    public List<ChatDTO> getMutualMatches(Long currentUserId) {
        List<Match> matchesFromSender = matchService.getMatchesBySenderIdAndIsLiked(
                currentUserId,
                true
        );

        List<Match> matchesFromReceiver = matchService.getMatchesByReceiverIdAndIsLiked(
                currentUserId,
                true
        );

        List<ChatDTO> chatDTOs = new ArrayList<>();

        for (Match matchFromSender : matchesFromSender) {
            for (Match matchFromReceiver : matchesFromReceiver) {
                if (
                        matchFromSender.getReceiver().getId().equals(
                                matchFromReceiver.getSender().getId()
                        )
                ) {
                    Optional<String> chatId = chatRoomService.getChatId(
                            matchFromSender.getSender().getId(),
                            matchFromSender.getReceiver().getId(),
                            true
                    );

                    ChatMessage lastMessage = chatMessageService.findLastMessageInChat(chatId.get());


                    chatDTOs.add(ChatDTO.builder()
                            .user(
                                    modelMapper.map(
                                            matchFromSender.getReceiver(),
                                            UserDTO.class
                                    )
                            )
                            .lastMessage(lastMessage)
                            .build()
                    );
                }
            }
        }

        return chatDTOs;
    }
}
