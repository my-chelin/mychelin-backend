package com.a206.mychelin.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;


import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@DynamicInsert
@Entity
public class UserPreference {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public int id;

    public String userId;

    public int sweet;
    public int salty;
    public int sour;
    public int oily;
    public int spicy;

    public int challenging;
    public int planning;
    public int sociable;
    public int sensitivity;

    @Builder
    public UserPreference(String userId, int sweet, int salty, int sour, int oily, int spicy, int challenging, int planning, int sociable, int sensitivity) {
        this.userId = userId;
        this.sweet = sweet;
        this.salty = salty;
        this.sour = sour;
        this.oily = oily;
        this.spicy = spicy;
        this.challenging = challenging;
        this.planning = planning;
        this.sociable = sociable;
        this.sensitivity = sensitivity;
    }
}