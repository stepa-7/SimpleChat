package com.stepa7.webchat.model.dto;


import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SignupDto {
    private String login;
    private String password;
    private String email;
}
