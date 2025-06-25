package com.example.norush.domain;

import com.example.norush.common.entity.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "members")
@Getter @Builder
@NoArgsConstructor
@AllArgsConstructor
public class Member extends BaseEntity {

    @Id
    private String id;

    private String email;
    private String password;

    private String username;
    private Boolean isValid;

    private List<String> favorList;
    private String profilePicture;

    private Integer age;
    private String gender;
    private String role;

    public void changePassword(String password) {
        this.password = password;
    }

    public void changedProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }
}
