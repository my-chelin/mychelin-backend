package com.a206.mychelin.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Date;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@DynamicInsert
@Entity
public class NoticeFollow {

    @Id
    private int id;

    private String userId;

    private String followingId;

    private boolean isRead;

    private Date addTime;

    public void readNotice() {
        isRead = true;
    }
}