package org.example.bereal.repository;

import org.example.bereal.model.Friendship;
import org.example.bereal.model.Friendship.FriendshipStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FriendshipRepository extends JpaRepository<Friendship, Long> {

    Optional<Friendship> findByUserIdAndFriendId(Long userId, Long friendId);

    List<Friendship> findByUserIdAndStatus(Long userId, FriendshipStatus status);

    List<Friendship> findByFriendIdAndStatus(Long friendId, FriendshipStatus status);

    @Query("SELECT f FROM Friendship f WHERE " +
            "(f.userId = :userId OR f.friendId = :userId) AND f.status = :status")
    List<Friendship> findAllFriendships(Long userId, FriendshipStatus status);

    @Query("SELECT CASE WHEN COUNT(f) > 0 THEN true ELSE false END FROM Friendship f " +
            "WHERE ((f.userId = :userId AND f.friendId = :friendId) OR " +
            "(f.userId = :friendId AND f.friendId = :userId)) AND f.status = 'ACCEPTED'")
    boolean areFriends(Long userId, Long friendId);
}