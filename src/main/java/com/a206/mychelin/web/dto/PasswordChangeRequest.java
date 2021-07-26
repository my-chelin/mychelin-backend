package com.a206.mychelin.web.dto;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class PasswordChangeRequest {
    String password;
    String newPassword;
}