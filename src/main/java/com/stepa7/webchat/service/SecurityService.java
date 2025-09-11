package com.stepa7.webchat.service;

import com.stepa7.webchat.model.dto.SigninDto;
import com.stepa7.webchat.model.dto.SignupDto;

public interface SecurityService {
    String registerUser(SignupDto signupDto);

    String loginUser(SigninDto signinDto);

    String logoutUser();
}
