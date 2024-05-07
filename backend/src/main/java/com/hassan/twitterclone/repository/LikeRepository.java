package com.hassan.twitterclone.repository;

import com.hassan.twitterclone.entity.LikeEntity;
import com.hassan.twitterclone.entity.TweetEntity;
import com.hassan.twitterclone.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface LikeRepository extends JpaRepository<LikeEntity, Long> {
    Optional<LikeEntity> findByUserAndTweet(UserEntity user, TweetEntity tweet);
    Optional<List<LikeEntity>> findAllByUser(UserEntity user);
}
