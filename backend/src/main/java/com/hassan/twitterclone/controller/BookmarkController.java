package com.hassan.twitterclone.controller;

import com.hassan.twitterclone.service.TweetService;
import com.hassan.twitterclone.dto.LikeRetweetBookmarkDto;
import com.hassan.twitterclone.dto.TweetResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/bookmarks")
@RequiredArgsConstructor
public class BookmarkController {

    private final TweetService tweetService;

    @PostMapping("/bookmark")
    public ResponseEntity<HttpStatus> bookmark(@RequestBody LikeRetweetBookmarkDto likeRetweetBookmarkDto){
        this.tweetService.bookmark(likeRetweetBookmarkDto);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{username}")
    public ResponseEntity<List<TweetResponseDto>> getBookmarks(@PathVariable String username){

        return ResponseEntity.ok(this.tweetService.getBookmarksByUsername(username));
    }

    @PostMapping("/is-bookmarked")
    public ResponseEntity<Boolean> isBookmarked(@RequestBody LikeRetweetBookmarkDto likeRetweetBookmarkDto){
        return ResponseEntity.ok(this.tweetService.isBookmarked(likeRetweetBookmarkDto));
    }
}
