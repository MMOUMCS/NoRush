package com.example.norush.config;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MongoDataInitalizer implements CommandLineRunner {

    private final MongoTemplate mongoTemplate;

    public void run(String... args) throws Exception {
        mongoTemplate.getDb().drop();

        // 특정 컬렉션만 삭제할 경우
        // mongoTemplate.dropCollection("members");
    }
}
