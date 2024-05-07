package com.hassan.twitterclone.controller;

import com.hassan.twitterclone.dto.*;
import com.hassan.twitterclone.service.AuthenticationService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/auth")
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping("/sign-up")
    public ResponseEntity<HttpStatus> signup(@RequestBody SignUpRequestDto signUpRequestDto){
        this.authenticationService.signup(signUpRequestDto);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/sign-in")
    public ResponseEntity<LoginResponseDto> login(@RequestBody LoginRequestDto loginRequestDto){
        return ResponseEntity.ok().body(this.authenticationService.login(loginRequestDto));
    }

    @PostMapping("/logout")
    public ResponseEntity<HttpStatus> logout(@RequestBody RefreshTokenDto refreshTokenDto){
        this.authenticationService.logout(refreshTokenDto);
        return ResponseEntity.ok().build();
    };

    @PostMapping("/refresh-token")
    public ResponseEntity<LoginResponseDto> refreshToken(@RequestBody RefreshTokenDto refreshTokenDto ){
        return ResponseEntity.ok().body(this.authenticationService.refreshToken(refreshTokenDto));
    }

    @GetMapping("/usernames")
    public ResponseEntity<List<String>> findAllUsernames(){
        List<String> usernames = this.authenticationService.findAllUsernames();
        return ResponseEntity.ok(usernames);
    }

    @PatchMapping("/changePassword")
    public ResponseEntity<?> changePassword(@RequestBody ChangePasswordDto request, Principal connectedUser){
        authenticationService.changePassword(request, connectedUser);
        return ResponseEntity.ok().build();
    }
}
