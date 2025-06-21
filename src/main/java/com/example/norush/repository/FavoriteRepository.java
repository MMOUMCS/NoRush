package com.example.norush.repository;

import com.example.norush.domain.Favorite;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface FavoriteRepository extends MongoRepository<Favorite, String> {

    List<Favorite> findByUserId(String userId);
    Optional<Favorite> findByIdAndUserId(String id, String userId);
    Optional<Favorite> findByUserIdAndName(String userId, String name);
}