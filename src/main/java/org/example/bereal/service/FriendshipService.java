package org.example.bereal.service;

import org.example.bereal.exception.UnauthorizedException;
import org.example.bereal.model.Friendship;
import org.example.bereal.model.Friendship.FriendshipStatus;
import org.example.bereal.repository.FriendshipRepository;
import org.example.bereal.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FriendshipService {

    private static final Logger log = LoggerFactory.getLogger(FriendshipService.class);

    private final FriendshipRepository friendshipRepository;
    private final UserRepository userRepository;

    public FriendshipService(FriendshipRepository friendshipRepository,
                             UserRepository userRepository) {
        this.friendshipRepository = friendshipRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public Friendship sendFriendRequest(Long userId, Long friendId) {
        log.info("User {} sending friend request to {}", userId, friendId);

        if (userId.equals(friendId)) {
            throw new IllegalArgumentException("Cannot send friend request to yourself");
        }

        // Проверяем, существует ли пользователь
        if (!userRepository.existsById(friendId)) {
            throw new IllegalArgumentException("User not found");
        }

        // Проверяем, нет ли уже запроса
        if (friendshipRepository.findByUserIdAndFriendId(userId, friendId).isPresent()) {
            throw new IllegalArgumentException("Friend request already exists");
        }

        // Проверяем обратный запрос (может друг уже отправил нам)
        if (friendshipRepository.findByUserIdAndFriendId(friendId, userId).isPresent()) {
            throw new IllegalArgumentException("This user has already sent you a request");
        }

        Friendship friendship = new Friendship();
        friendship.setUserId(userId);
        friendship.setFriendId(friendId);
        friendship.setStatus(FriendshipStatus.PENDING);

        return friendshipRepository.save(friendship);
    }

    @Transactional
    public Friendship acceptFriendRequest(Long userId, Long requesterId) {
        log.info("User {} accepting friend request from {}", userId, requesterId);

        Friendship friendship = friendshipRepository
                .findByUserIdAndFriendId(requesterId, userId)
                .orElseThrow(() -> new IllegalArgumentException("Friend request not found"));

        if (friendship.getStatus() != FriendshipStatus.PENDING) {
            throw new IllegalArgumentException("Request is not pending");
        }

        friendship.setStatus(FriendshipStatus.ACCEPTED);
        friendship.setAcceptedAt(LocalDateTime.now());

        return friendshipRepository.save(friendship);
    }

    @Transactional
    public void rejectFriendRequest(Long userId, Long requesterId) {
        log.info("User {} rejecting friend request from {}", userId, requesterId);

        Friendship friendship = friendshipRepository
                .findByUserIdAndFriendId(requesterId, userId)
                .orElseThrow(() -> new IllegalArgumentException("Friend request not found"));

        if (friendship.getStatus() != FriendshipStatus.PENDING) {
            throw new IllegalArgumentException("Request is not pending");
        }

        friendship.setStatus(FriendshipStatus.REJECTED);
        friendshipRepository.save(friendship);
    }

    @Transactional
    public void removeFriend(Long userId, Long friendId) {
        log.info("User {} removing friend {}", userId, friendId);

        // Ищем в обе стороны
        Friendship friendship = friendshipRepository
                .findByUserIdAndFriendId(userId, friendId)
                .or(() -> friendshipRepository.findByUserIdAndFriendId(friendId, userId))
                .orElseThrow(() -> new IllegalArgumentException("Friendship not found"));

        // Проверяем, что это один из участников дружбы
        if (!friendship.getUserId().equals(userId) && !friendship.getFriendId().equals(userId)) {
            throw new UnauthorizedException("You cannot remove this friendship");
        }

        friendshipRepository.delete(friendship);
    }

    public List<Long> getFriendIds(Long userId) {
        List<Friendship> friendships = friendshipRepository
                .findAllFriendships(userId, FriendshipStatus.ACCEPTED);

        return friendships.stream()
                .map(f -> f.getUserId().equals(userId) ? f.getFriendId() : f.getUserId())
                .collect(Collectors.toList());
    }

    public List<Friendship> getPendingRequests(Long userId) {
        return friendshipRepository.findByFriendIdAndStatus(userId, FriendshipStatus.PENDING);
    }

    public List<Friendship> getSentRequests(Long userId) {
        return friendshipRepository.findByUserIdAndStatus(userId, FriendshipStatus.PENDING);
    }

    public boolean areFriends(Long userId, Long friendId) {
        return friendshipRepository.areFriends(userId, friendId);
    }
}