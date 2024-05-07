package com.hassan.twitterclone.controller;

import com.hassan.twitterclone.service.TweetService;
import com.hassan.twitterclone.dto.LikeRetweetBookmarkDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("/retweets")
@RequiredArgsConstructor
public class RetweetController {

    private final TweetService tweetService;

    @PostMapping("/retweet")
    public ResponseEntity<HttpStatus> postRetweet(@RequestBody LikeRetweetBookmarkDto likeRetweetBookmarkDto){
        this.tweetService.retweet(likeRetweetBookmarkDto);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/is-retweeted")
    public ResponseEntity<Boolean> isRetweeted(@RequestBody LikeRetweetBookmarkDto likeRetweetBookmarkDto){
        return ResponseEntity.ok(this.tweetService.isRetweeted(likeRetweetBookmarkDto));
    }
}
