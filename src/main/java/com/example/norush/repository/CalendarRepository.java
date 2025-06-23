package com.example.norush.repository;

import com.example.norush.domain.Calendar;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface CalendarRepository extends MongoRepository<Calendar, String> {

    List<Calendar> findByUserId(String userId); 
    List<Calendar> findByUserIdAndYearAndMonth(String userId, Integer year, Integer month);
    Optional<Calendar> findByIdAndUserId(String id, String userId);
}