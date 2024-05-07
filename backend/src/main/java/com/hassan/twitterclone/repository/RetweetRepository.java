package com.hassan.twitterclone.repository;

import com.hassan.twitterclone.entity.RetweetEntity;
import com.hassan.twitterclone.entity.TweetEntity;
import com.hassan.twitterclone.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RetweetRepository extends JpaRepository<RetweetEntity, Long> {
    Optional<RetweetEntity> findByUserAndTweet(UserEntity user, TweetEntity tweet);
    Optional<RetweetEntity> findAllByTweet(TweetEntity tweet);
    Optional<List<RetweetEntity>> findAllByUser(UserEntity user);
}
