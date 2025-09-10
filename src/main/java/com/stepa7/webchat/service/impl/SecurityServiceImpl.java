package com.stepa7.webchat.service.impl;

import com.stepa7.webchat.exception.BadCredentialsException;
import com.stepa7.webchat.model.dto.SigninDto;
import com.stepa7.webchat.model.dto.SignupDto;
import com.stepa7.webchat.model.entity.User;
import com.stepa7.webchat.repository.UserRepository;
import com.stepa7.webchat.security.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SecurityServiceImpl {
    private final UserRepository userRepository;

    public String registerUser(SignupDto signupDto) {
        if (userRepository.existsUserByLogin(signupDto.getLogin())) {
            return "login_exists";
        }
        if (userRepository.existsUserByMail(signupDto.getEmail())) {
            return "email_exists";
        }
        String hashed = SecurityUtil.hashPassword(signupDto.getPassword());
        User user = new User();
        user.setLogin(signupDto.getLogin());
        user.setMail(signupDto.getEmail());
        user.setPassword(hashed);
        userRepository.save(user);
        return "success";
    }

    public String loginUser(SigninDto signinDto) {
        User user = userRepository.findUserByLogin(signinDto.getLogin()).orElseThrow();
        String hashedPassword = user.getPassword();
        if (!SecurityUtil.checkPassword(signinDto.getPassword(), hashedPassword)) {
            return "invalid_credentials";
        }

        // TODO подключение к серверу

        return "success";
    }
}
