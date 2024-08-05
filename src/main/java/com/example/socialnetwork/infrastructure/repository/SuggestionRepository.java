package com.example.socialnetwork.infrastructure.repository;

import com.example.socialnetwork.infrastructure.entity.Suggestion;
import com.example.socialnetwork.infrastructure.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SuggestionRepository extends JpaRepository<Suggestion, Long>{
    @Query("SELECT r FROM Suggestion r " +
            "WHERE (r.user = :user OR r.friend = :user) " +
            "AND r.status = 'NONE' " +
            "ORDER BY r.point DESC, r.mutualFriends DESC ")
    List<Suggestion> findByUserOrFriend(@Param("user") User user);

    @Query("SELECT r FROM Suggestion r " +
            "WHERE (r.user = :user1 AND r.friend = :user2) " +
             "OR (r.user = :user2 AND r.friend = :user1)")
    Suggestion findByUserAndFriend(@Param("user1") User user1, @Param("user2") User user2);

    @Query("SELECT r FROM Suggestion r " +
            "WHERE (r.user = :user OR r.friend = :user) " +
            "AND r.status <> 'BLOCK' " +
            "ORDER BY r.status ASC, r.point DESC, r.mutualFriends DESC ")
    List<Suggestion> searchUser(@Param("user") User user);
}
