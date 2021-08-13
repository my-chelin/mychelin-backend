package com.a206.mychelin.domain.entity;

import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.util.Date;

@NoArgsConstructor
@Getter
@Entity
@Table(name = "user")
@ToString
@DynamicUpdate
@DynamicInsert
public class User {
    @Builder
    public User(String id, String nickname, String password, String phoneNumber) {
        this.id = id;
        this.nickname = nickname;
        this.password = password;
        this.phoneNumber = phoneNumber;
    }

    @Id
    @Column(name = "id", nullable = false)
    private String id;

    @Column(name = "nickname", nullable = false)
    private String nickname;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "phone_number", nullable = false)
    private String phoneNumber;

    @Column(name = "create_date", nullable = true)
    private Date createDate;

    @Column(name = "profile_image", nullable = true)
    private String profileImage;

    @Column(name = "bio", nullable = true)
    private String bio;

    @Column(name = "withdraw", nullable = true)
    private boolean withdraw;

    @Column(name = "role", nullable = true)
    private String role;

    @Column(name = "is_private", nullable = true)
    private boolean isPrivate;

    public void updateInfo(String nickname, String bio, String phoneNumber) {
        this.nickname = nickname;
        this.bio = bio;
        this.phoneNumber = phoneNumber;
    }

    public void userImageUpdate(String imagePath) {
        this.profileImage = imagePath;
    }

    public void changePassword(String password) {
        this.password = password;
    }

    public void userWithdraw() {
        this.withdraw = true;
    }

    public void updateIsPrivate() {
        this.isPrivate = !this.isPrivate;
    }
}