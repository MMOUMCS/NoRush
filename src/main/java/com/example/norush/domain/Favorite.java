package com.example.norush.domain;

import com.example.norush.common.entity.BaseEntity; // Member와 동일하게 BaseEntity 상속
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDateTime;

@Document(collection = "favorites")
@Getter @Builder
@NoArgsConstructor
@AllArgsConstructor
public class Favorite extends BaseEntity {
    @Id
    private String id;

    private String userId; // email?
    private String name;
    private String type;
    private String targetId;
    private LocalDateTime createdAt;
    private String memo;

    public void update(String name, String type , String targetId, String memo){
        if (name != null){
            this.name = name;
        }
        if (type != null){
            this.type=type;
        }
        if(targetId !=null){
            this.targetId=targetId;
        }
        if (memo!=null){
            this.memo = memo;
        }
    }
}