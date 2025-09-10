package com.stepa7.webchat.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SigninDto {
    private String login;
    private String password;
}
