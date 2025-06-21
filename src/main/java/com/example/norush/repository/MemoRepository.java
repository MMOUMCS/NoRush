package com.example.norush.repository;

import com.example.norush.domain.Memo;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface MemoRepository extends MongoRepository<Memo, String> {

    List<Memo> findByUserId(String userId);
    Optional<Memo> findByIdAndUserId(String id, String userId);
    Optional<Memo> findByUserIdAndTitle(String userId, String title);
}