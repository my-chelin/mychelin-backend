package com.a206.mychelin.domain.repository;

import com.a206.mychelin.domain.entity.BookmarkPlace;
import com.a206.mychelin.web.dto.Bookmark;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BookmarkRepository extends JpaRepository<BookmarkPlace, Integer> {
    Optional<BookmarkPlace> findBookmarkPlacesByUserId(String userId);

}
