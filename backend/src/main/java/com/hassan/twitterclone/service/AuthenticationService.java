package com.hassan.twitterclone.service;

import com.hassan.twitterclone.dto.*;
import com.hassan.twitterclone.entity.UserEntity;
import com.hassan.twitterclone.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.aspectj.weaver.ast.Var;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.security.Principal;
import java.time.Instant;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    private final Pattern usernamePattern = Pattern.compile("^[A-Za-z]{2}.{1,10}$");

    private final Pattern passwordPattern = Pattern.compile("^[A-Z].{4,15}$");

    private final Pattern emailPattern = Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{3,}$");

    public void signup(SignUpRequestDto signUpRequestDto) {
        if (!usernamePattern.matcher(signUpRequestDto.getUsername()).matches()) {
            throw new IllegalArgumentException(
                    "Username must start with at least 2 letters and can have up to 10 characters.");
        }

        if (!passwordPattern.matcher(signUpRequestDto.getPassword()).matches()) {
            throw new IllegalArgumentException(
                    "Password must start with an uppercase letter and be between 5 to 15 characters.");
        }

        if (!emailPattern.matcher(signUpRequestDto.getEmail()).matches()) {
            throw new IllegalArgumentException("Invalid email format.");
        }

        if (userRepository.existsByUsername(signUpRequestDto.getUsername())) {
            throw new IllegalArgumentException("Username is already taken.");
        }

        if (userRepository.existsByEmail(signUpRequestDto.getEmail())) {
            throw new IllegalArgumentException("Email is already registered.");
        }

        userRepository.save(
                UserEntity.builder()
                        .firstName(signUpRequestDto.getFirstName())
                        .lastName(signUpRequestDto.getLastName())
                        .username(signUpRequestDto.getUsername())
                        .email(signUpRequestDto.getEmail())
                        .password(passwordEncoder.encode(signUpRequestDto.getPassword()))
                        .profilePicturePath("C:/Users/pc/Downloads/profile.jpg")
                        .bannerPicturePath("C:/Users/pc/Downloads/profile.jpg")
                        .build());
    }

    public LoginResponseDto login(LoginRequestDto loginRequestDto) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequestDto.getUsername(), loginRequestDto.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String accessToken = jwtService.generateToken(authentication);
        String refreshToken = jwtService.generateRefreshToken().getRefreshToken();
        String username = loginRequestDto.getUsername();

        return LoginResponseDto.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .username(username)
                .expiresAt(Instant.now().plusMillis(jwtService.getJwtExpirationInMillis()))
                .build();
    }

    public void logout(RefreshTokenDto refreshTokenDto) {
        jwtService.deleteRefreshToken(refreshTokenDto.getRefreshToken());
        SecurityContextHolder.clearContext();
    }

    public LoginResponseDto refreshToken(RefreshTokenDto refreshTokenDto) {
        this.jwtService.validateRefreshToken(refreshTokenDto.getRefreshToken());
        String accessToken = this.jwtService.generateTokenWithUsername(refreshTokenDto.getUsername());
        return LoginResponseDto.builder()
                .accessToken(accessToken)
                .refreshToken(refreshTokenDto.getRefreshToken())
                .username(refreshTokenDto.getUsername())
                .expiresAt(Instant.now().plusMillis(jwtService.getJwtExpirationInMillis()))
                .build();
    }

    public UserEntity getUserFromJwt() {
        Jwt principal = (Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        return userRepository.findByUsername(principal.getSubject()).orElseThrow();
    }

    public List<String> findAllUsernames() {
        return this.userRepository.findAll().stream().map(user -> user.getUsername()).collect(Collectors.toList());
    }

    public void changePassword(ChangePasswordDto request, Principal connectedUser) {
        String username = connectedUser.getName();

        UserEntity user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        if (!passwordEncoder.matches(request.getOldPassword(), user.getPassword())) {
            throw new IllegalArgumentException("Invalid old password");
        }

        if (!passwordPattern.matcher(request.getNewPassword()).matches()) {
            throw new IllegalArgumentException(
                    "Password must start with an uppercase letter and be between 5 to 15 characters.");
        }

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));

        userRepository.save(user);
    }

}
