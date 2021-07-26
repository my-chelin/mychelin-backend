package com.a206.mychelin.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Place {
    @Id
    private int id;
    private String name;
    private String description;
    private double latitude;
    private double longitude;
    private String phone;
    private String location;
    private String operationHours;
    private int categoryId;
    private String image;
}