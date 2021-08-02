package com.a206.mychelin.domain.entity;

import lombok.*;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;
import java.util.Date;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "placelist")
@DynamicInsert
@ToString
public class PlaceList {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String title;

    private Date createDate;

    private String user_id;

    public void clear() {
        id = 0;
        createDate = null;
    }
}