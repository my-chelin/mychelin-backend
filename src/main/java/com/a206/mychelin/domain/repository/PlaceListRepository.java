package com.a206.mychelin.domain.repository;

import com.a206.mychelin.domain.entity.PlaceList;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PlaceListRepository extends JpaRepository<PlaceList,Integer> {

    Optional<PlaceList> findById(int id);

}
