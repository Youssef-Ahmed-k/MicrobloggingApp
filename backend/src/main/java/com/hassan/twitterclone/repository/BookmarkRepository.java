package com.hassan.twitterclone.repository;

import com.hassan.twitterclone.entity.BookmarkEntity;
import com.hassan.twitterclone.entity.TweetEntity;
import com.hassan.twitterclone.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BookmarkRepository extends JpaRepository<BookmarkEntity, Long> {

    Optional<BookmarkEntity> findByUserAndTweet(UserEntity user, TweetEntity tweet);
    Optional<List<BookmarkEntity>> findAllByUser(UserEntity user);
}
