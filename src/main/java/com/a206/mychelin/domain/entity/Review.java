package com.a206.mychelin.domain.entity;

import com.a206.mychelin.web.dto.ReviewEditRequest;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Date;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@DynamicInsert
@DynamicUpdate
@Builder
@ToString
public class Review {
    @Id
    private int id;

    private float starRate;

    private String content;

    private String userId;

    private Date createDate;

    private int placeId;

    private String image;

    public void editReview(ReviewEditRequest reviewEditRequest) {
        this.starRate = reviewEditRequest.getStarRate();
        this.content = reviewEditRequest.getContent();
        this.createDate = new Date();
        this.image = reviewEditRequest.getImage();
    }

    public void reviewImageUpdate(String imagePath) {
        this.image = imagePath;
    }
}