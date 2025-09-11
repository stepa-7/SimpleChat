package com.stepa7.webchat.service.impl;

import com.stepa7.webchat.model.dto.SigninDto;
import com.stepa7.webchat.model.dto.SignupDto;
import com.stepa7.webchat.model.entity.User;
import com.stepa7.webchat.repository.UserRepository;
import com.stepa7.webchat.security.SecurityUtil;
import com.stepa7.webchat.service.ChatService;
import com.stepa7.webchat.service.SecurityService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SecurityServiceImpl implements SecurityService {
    private final UserRepository userRepository;
    private final ChatService chatService;

    @Override
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

    @Override
    public String loginUser(SigninDto signinDto) {
        User user = userRepository.findUserByLogin(signinDto.getLogin()).orElseThrow();
        String hashedPassword = user.getPassword();
        if (!SecurityUtil.checkPassword(signinDto.getPassword(), hashedPassword)) {
            return "invalid_credentials";
        }

        try {
            chatService.connectToServer(signinDto.getLogin());
        } catch (Exception e) {
            return "failed to connect to server";
        }

        return "success";
    }

    @Override
    public String logoutUser() {
        chatService.disconnectFromServer();

        // TODO обработать ошибки
        return "success";
    }
}
