package com.a206.mychelin.domain.repository;

import com.a206.mychelin.domain.entity.User;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, String> {
    Optional<User> findUserById(String id);

    Optional<User> findUserByNickname(String nickname);

    void deleteUsersById(String id);

    @Query(value = "select u.profile_image\n" +
            "from user u, (select pi.contributor_id\n" +
            "from placelist_item pi\n" +
            "where pi.placelist_id = :placeListId\n" +
            "group by pi.contributor_id\n" +
            "order by count(place_id) desc\n" +
            "limit 2) p\n" +
            "where u.id = p.contributor_id;", nativeQuery = true)
    List<String> findContributedUserProfilesByPlaceListId(@Param("placeListId") int placeListId);
}