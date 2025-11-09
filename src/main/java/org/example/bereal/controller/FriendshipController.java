package org.example.bereal.controller;

import org.example.bereal.dto.FriendshipDTO;
import org.example.bereal.mapper.FriendshipMapper;
import org.example.bereal.model.Friendship;
import org.example.bereal.security.JwtUtil;
import org.example.bereal.service.FriendshipService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/friends")
public class FriendshipController {

    private final FriendshipService friendshipService;
    private final JwtUtil jwtUtil; // ← Добавляем

    public FriendshipController(FriendshipService friendshipService, JwtUtil jwtUtil) {
        this.friendshipService = friendshipService;
        this.jwtUtil = jwtUtil; // ← Инжектим
    }

    @PostMapping("/request/{friendId}")
    public ResponseEntity<FriendshipDTO> sendFriendRequest(
            @PathVariable Long friendId,
            @RequestHeader("Authorization") String authHeader) {

        Long userId = extractUserIdFromToken(authHeader);
        Friendship friendship = friendshipService.sendFriendRequest(userId, friendId);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(FriendshipMapper.toDto(friendship));
    }

    @PostMapping("/accept/{requesterId}")
    public ResponseEntity<FriendshipDTO> acceptFriendRequest(
            @PathVariable Long requesterId,
            @RequestHeader("Authorization") String authHeader) {

        Long userId = extractUserIdFromToken(authHeader);
        Friendship friendship = friendshipService.acceptFriendRequest(userId, requesterId);

        return ResponseEntity.ok(FriendshipMapper.toDto(friendship));
    }

    @PostMapping("/reject/{requesterId}")
    public ResponseEntity<Void> rejectFriendRequest(
            @PathVariable Long requesterId,
            @RequestHeader("Authorization") String authHeader) {

        Long userId = extractUserIdFromToken(authHeader);
        friendshipService.rejectFriendRequest(userId, requesterId);

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{friendId}")
    public ResponseEntity<Void> removeFriend(
            @PathVariable Long friendId,
            @RequestHeader("Authorization") String authHeader) {

        Long userId = extractUserIdFromToken(authHeader);
        friendshipService.removeFriend(userId, friendId);

        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<Long>> getFriends(
            @RequestHeader("Authorization") String authHeader) {
        Long userId = extractUserIdFromToken(authHeader);
        List<Long> friendIds = friendshipService.getFriendIds(userId);

        return ResponseEntity.ok(friendIds);
    }

    @GetMapping("/requests/pending")
    public ResponseEntity<List<FriendshipDTO>> getPendingRequests(
            @RequestHeader("Authorization") String authHeader) {
        Long userId = extractUserIdFromToken(authHeader);
        List<FriendshipDTO> requests = friendshipService.getPendingRequests(userId)
                .stream()
                .map(FriendshipMapper::toDto)
                .collect(Collectors.toList());

        return ResponseEntity.ok(requests);
    }

    @GetMapping("/requests/sent")
    public ResponseEntity<List<FriendshipDTO>> getSentRequests(
            @RequestHeader("Authorization") String authHeader) {
        Long userId = extractUserIdFromToken(authHeader);
        List<FriendshipDTO> requests = friendshipService.getSentRequests(userId)
                .stream()
                .map(FriendshipMapper::toDto)
                .collect(Collectors.toList());

        return ResponseEntity.ok(requests);
    }

    private Long extractUserIdFromToken(String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new IllegalStateException("Invalid Authorization header");
        }

        String token = authHeader.substring(7);
        return jwtUtil.extractUserId(token);
    }
}