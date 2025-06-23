package com.example.norush.domain;

import com.example.norush.common.entity.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "Memo")
@Getter @Builder
@NoArgsConstructor
@AllArgsConstructor
public class Memo extends BaseEntity {

    @Id
    private String id;

    private String userId;

    private String title;
    private String content;
    private LocalDateTime timestamp;

    public void update(String title, String content) {
        if (title != null) {
            this.title = title;
        }
        if (content != null) {
            this.content = content;
        }
    }
}