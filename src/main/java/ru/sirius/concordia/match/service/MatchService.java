package ru.sirius.concordia.match.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.sirius.concordia.match.config.DatingConfig;
import ru.sirius.concordia.match.ml.SimilarUsersHandler;
import ru.sirius.concordia.match.model.Match;
import ru.sirius.concordia.match.model.dto.MatchDTO;
import ru.sirius.concordia.match.repository.MatchRepositoryInterface;
import ru.sirius.concordia.user.model.User;
import ru.sirius.concordia.user.service.UserService;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MatchService {
    private final MatchRepositoryInterface matchRepository;

    private final UserService userService;

    private final SimilarUsersHandler similarUsersHandler;

    private final DatingConfig datingConfig;

    public Match createOrUpdateMatch(MatchDTO matchDTO) {
        User sender = userService.getUserById(matchDTO.getSender().getId());
        User receiver = userService.getUserById(matchDTO.getReceiver().getId());

        Match match = matchRepository.findBySenderIdAndReceiverId(
                sender.getId(),
                receiver.getId()
        );

        if (match != null) {
            match.setIsLiked(matchDTO.getIsLiked());
        } else {
            match = Match.builder()
                    .sender(sender)
                    .receiver(receiver)
                    .isLiked(matchDTO.getIsLiked())
                    .build();
        }

        return matchRepository.save(match);
    }

    public List<User> getSimilarUsers(
            Long userId,
            Long countNeighbors,
            Double mutationChance
    ) {
        List<Long> recentInterestedUserIds = this.getRecentInterestedUserIds(
                userId
        );

        List<Long> rejectedLikersIds = matchRepository.findReceiverIdsBySenderIdAndIsLiked(
                userId,
                false
        );

        long remainingCount = countNeighbors - recentInterestedUserIds.size();

        Map<Integer, Double> similarUsers = remainingCount > 0
                ? similarUsersHandler.findSimilarUsers(userId, remainingCount, mutationChance)
                : Collections.emptyMap();

        Set<Long> combinedUserIds = new LinkedHashSet<>();

        combinedUserIds.addAll(
                recentInterestedUserIds
        );

        similarUsers.keySet().stream()
                .map(Long::valueOf)
                .filter(id -> !id.equals(userId))
                .filter(id -> !rejectedLikersIds.contains(id))
                .forEach(combinedUserIds::add);

        List<Long> finalUserIds = combinedUserIds.stream()
                .limit(countNeighbors)
                .collect(Collectors.toList());

        return userService.getUsersByIds(
                finalUserIds
        );
    }

    public List<User> getLastedMatchesBySenderId(
            Long senderId,
            int limit
    ) {
        return matchRepository.findMatchesBySenderId(
                senderId,
                PageRequest.of(0, limit)
        );
    }

    public List<Match> getMatchesBySenderIdAndIsLiked(
            Long senderId,
            Boolean isLiked
    ) {
        return matchRepository.findBySenderIdAndIsLiked(
                senderId,
                isLiked
        );
    }

    public List<Match> getMatchesByReceiverIdAndIsLiked(
            Long receiverId,
            Boolean isLiked
    ) {
        return matchRepository.findByReceiverIdAndIsLiked(
                receiverId,
                isLiked
        );
    }

    public List<Long> getRecentInterestedUserIds(Long userId) {
        return matchRepository.findLikedUsersWithinDaysAgo(
                userId,
                LocalDateTime.now().minusDays(
                        datingConfig.getMatchCooldown()
                )
        );
    }
}
