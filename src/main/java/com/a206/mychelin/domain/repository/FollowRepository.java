package com.a206.mychelin.domain.repository;

import com.a206.mychelin.domain.entity.FollowPK;
import com.a206.mychelin.domain.entity.Follow;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FollowRepository extends JpaRepository<Follow, FollowPK> {
    int countByUserId(String userId);

    int countByFollowingId(String followingId);
}