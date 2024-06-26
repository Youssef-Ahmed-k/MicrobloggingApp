package com.hassan.twitterclone.dto;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RefreshTokenDto {
    private String refreshToken;
    private String username;
}
