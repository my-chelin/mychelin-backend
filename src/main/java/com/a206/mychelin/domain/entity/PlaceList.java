package com.a206.mychelin.domain.entity;

import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.omg.CORBA.PRIVATE_MEMBER;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name="placelist")
@DynamicInsert
@ToString
public class PlaceList {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String title;

    private Date createDate;

}
