package com.a206.mychelin.domain.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.util.Date;

@Getter
@Table(name="comment")
@ToString
@DynamicUpdate
@NoArgsConstructor
@Entity
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
//
//    private int postId;
    private String writerId;

    @Column(name="message", nullable = false)
    private String message;


    @Column(name="create_date", insertable = false, updatable = false)
    private Date createDate;

    //Parent 필드 추가
//    @ManyToOne
//    @JoinColumn(name="post_id")
//    private Post post;
    private int postId;

    @Builder
    public Comment(String writerId, String message, int postId) {
        this.writerId = writerId;
        this.message = message;
        this.postId = postId;
    }
}
