package com.stepa7.webchat.controller;

import com.stepa7.webchat.model.dto.SigninDto;
import com.stepa7.webchat.model.dto.SignupDto;
import com.stepa7.webchat.service.SecurityService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class SecurityController {
    private final SecurityService securityService;

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody SignupDto signupDto) {
        String result = securityService.registerUser(signupDto);

        if (result.equals("success")) {
            return ResponseEntity.ok(Map.of("signup", "success"));
        }
        return ResponseEntity.badRequest().body(result);
    }


    @PostMapping("/signin")
    public ResponseEntity<?> signin(@RequestBody SigninDto signinDto) {
        String result = securityService.loginUser(signinDto);

        if (result.equals("success")) {
            return ResponseEntity.ok(Map.of("signin", "success"));
        }
        return ResponseEntity.badRequest().body(result);
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout() {
        securityService.logoutUser();
        return ResponseEntity.ok(Map.of("logout", "success"));
    }
}
