package com.a206.mychelin.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.ValueGenerationType;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@DynamicInsert
@Entity
public class UserPreference {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public int id;

    public String userId;

    public double sweet;
    public double salty;
    public double sour;
    public double oily;
    public double spicy;

    public boolean challenging;
    public boolean planning;
    public boolean networking;
    public boolean sensitive;

    @Builder
    public UserPreference(String userId, double sweet, double salty, double sour, double oily, double spicy) {
        this.userId = userId;
        this.sweet = sweet;
        this.salty = salty;
        this.sour = sour;
        this.oily = oily;
        this.spicy = spicy;
    }
}
