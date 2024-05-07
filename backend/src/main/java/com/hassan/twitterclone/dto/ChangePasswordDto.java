package com.hassan.twitterclone.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChangePasswordDto {
    private String oldPassword;
    private String newPassword;
}
