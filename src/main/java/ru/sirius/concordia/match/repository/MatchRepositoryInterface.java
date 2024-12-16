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

    @Query("SELECT m.receiver FROM Match m WHERE m.sender.id = :senderId ORDER BY m.updatedAt DESC")
    List<User> findMatchesBySenderId(Long senderId, Pageable pageable);

    @Query("SELECT u.id " +
            "FROM User u " +
            "WHERE u.id <> :userId " +
            "AND u.id NOT IN ( " +
            "   SELECT m.receiver.id " +
            "   FROM Match m " +
            "   WHERE m.sender.id = :userId " +
            "     AND m.isLiked = false " +
            "     AND m.updatedAt >= :thresholdDate " +
            ") " +
            "AND u.id NOT IN ( " +
            "   SELECT m.sender.id " +
            "   FROM Match m " +
            "   WHERE m.receiver.id = :userId " +
            "     AND m.isLiked = true " +
            ") " +
            "ORDER BY " +
            "   CASE WHEN u.id IN ( " +
            "       SELECT m.sender.id " +
            "       FROM Match m " +
            "       WHERE m.receiver.id = :userId " +
            "         AND m.isLiked = true " +
            "   ) THEN 1 ELSE 2 END, u.id ASC " +
            "LIMIT :limit")
    List<Long> findAvailableUserIds(
            @Param("userId") Long userId,
            @Param("thresholdDate") LocalDateTime thresholdDate,
            @Param("limit") Long limit
    );
}
