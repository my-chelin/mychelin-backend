package com.a206.mychelin.domain.entity;

import lombok.*;

import javax.persistence.*;
import java.util.Date;

@NoArgsConstructor
@Getter
@Entity
@Table(name="user")
@ToString
public class UserEntity {
    @Id
    @Column(name="id", nullable = false)
    private String id;
    @Column(name="nickname", nullable = false)
    private String nickname;
    @Column(name="password", nullable = false)
    private String password;
    @Column(name="gender", nullable = true)
    private char gender;
    @Column(name="phone_number", nullable = true)
    private String phoneNumber;
    @Column(name="report_count", nullable = true)
    private int reportCount;
    @Column(name="create_date", nullable = true)
    private Date createDate;
    @Column(name="profile_image", nullable = true)
    private String profileImage;
    @Column(name="bio", nullable = true)
    private String bio;
    @Column(name="withdraw", nullable = true)
    private boolean withdraw;
}
