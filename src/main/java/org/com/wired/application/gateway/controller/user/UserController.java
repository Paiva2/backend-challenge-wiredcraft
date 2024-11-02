package org.com.wired.application.gateway.controller.user;

import jakarta.validation.Valid;
import org.com.wired.application.gateway.input.AuthenticateUserInput;
import org.com.wired.application.gateway.input.RegisterUserInput;
import org.com.wired.application.gateway.output.GetProfileUserOutput;
import org.com.wired.application.gateway.output.RegisterUserOutput;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;

@RequestMapping("/api/user")
public interface UserController {
    @PostMapping("/register")
    ResponseEntity<RegisterUserOutput> register(@RequestBody @Valid RegisterUserInput registerUserInput);

    @PostMapping("/auth")
    ResponseEntity<Map<String, String>> login(@RequestBody @Valid AuthenticateUserInput input);

    @GetMapping("/profile")
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    ResponseEntity<GetProfileUserOutput> getProfile(Authentication authentication);
}