package ru.sirius.concordia.match.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.sirius.concordia.match.model.Match;
import ru.sirius.concordia.user.model.User;

import java.util.List;

@Repository
public interface MatchRepository extends JpaRepository<Match, Long> {
    Match findBySenderIdAndReceiverId(Long senderId, Long receiverId);
}