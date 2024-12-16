package ru.sirius.concordia.match.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.sirius.concordia.match.model.Match;
import ru.sirius.concordia.user.model.User;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface MatchRepositoryInterface extends JpaRepository<Match, Long> {
    Match findBySenderIdAndReceiverId(Long senderId, Long receiverId);

    List<Match> findBySenderIdAndIsLiked(Long senderId, Boolean isLiked);

    List<Match> findByReceiverIdAndIsLiked(Long receiverId, Boolean isLiked);

    @Query("SELECT m.receiver.id FROM Match m WHERE m.sender.id = :senderId AND m.isLiked = :isLiked")
    List<Long> findReceiverIdsBySenderIdAndIsLiked(
            @Param("senderId") Long senderId,
            @Param("isLiked") Boolean isLiked
    );

    @Query("SELECT m.receiver FROM Match m WHERE m.sender.id = :senderId ORDER BY m.updatedAt DESC")
    List<User> findMatchesBySenderId(Long senderId, Pageable pageable);

    @Query("SELECT m.sender.id " +
            "FROM Match m " +
            "WHERE m.receiver.id = :userId " +
            "AND m.isLiked = true " +
            "AND NOT EXISTS (" +
            "   SELECT 1 FROM Match r " +
            "   WHERE r.sender.id = :userId " +
            "   AND r.receiver.id = m.sender.id " +
            "   AND r.isLiked = false " +
            "   AND r.createdAt >= :daysAgo" +
            ") " +
            "AND m.createdAt >= :daysAgo")
    List<Long> findLikedUsersWithinDaysAgo(
            @Param("userId") Long userId,
            @Param("daysAgo") LocalDateTime daysAgo
    );

}
