package ru.sirius.concordia.match.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.sirius.concordia.match.model.Match;
import ru.sirius.concordia.user.model.User;
import org.springframework.data.domain.Pageable;

import java.util.List;

@Repository
public interface MatchRepository extends JpaRepository<Match, Long> {
    Match findBySenderIdAndReceiverId(Long senderId, Long receiverId);

    @Query("SELECT m.receiver FROM Match m WHERE m.sender.id = :senderId ORDER BY m.updatedAt DESC")
    List<User> findMatchesBySenderId(Long senderId, Pageable pageable);
}