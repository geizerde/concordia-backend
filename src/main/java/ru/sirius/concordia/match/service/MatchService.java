package ru.sirius.concordia.match.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.sirius.concordia.match.ml.java.SimilarUsersHandler;
import ru.sirius.concordia.match.model.Match;
import ru.sirius.concordia.match.model.dto.MatchDTO;
import ru.sirius.concordia.match.repository.MatchRepository;
import ru.sirius.concordia.user.model.User;
import ru.sirius.concordia.user.service.UserService;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MatchService {
    private final MatchRepository matchRepository;

    private final UserService userService;

    private final SimilarUsersHandler similarUsersHandler;

    public Match createOrUpdateMatch(MatchDTO matchDTO) {
        User sender = userService.getUserById(matchDTO.getSender().getId());
        User receiver = userService.getUserById(matchDTO.getReceiver().getId());

        Match match = matchRepository.findBySenderIdAndReceiverId(
                sender.getId(),
                receiver.getId()
        );

        if (match != null) {
            match.setIsLiked(matchDTO.getLike());
        } else {
            match = Match.builder()
                    .sender(sender)
                    .receiver(receiver)
                    .isLiked(matchDTO.getLike())
                    .build();
        }

        return matchRepository.save(match);
    }

    public List<User> getSimilarUsers(
            Long userId,
            Long countNeighbors,
            Double mutationChance
    ) {
        Map<Integer, Double> res = similarUsersHandler.findSimilarUsers(
                userId,
                countNeighbors,
                mutationChance
        );

        List<Long> similarUserIds = res.keySet().stream()
                .map(Long::valueOf)
                .filter(id -> !id.equals(userId))
                .collect(Collectors.toList());

       return userService.getUsersByIds(similarUserIds);
    }
}
