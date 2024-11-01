package org.com.wired.application.gateway.controller.user;

import jakarta.validation.Valid;
import org.com.wired.application.gateway.input.RegisterUserInput;
import org.com.wired.application.gateway.output.RegisterUserOutput;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/api/user")
public interface UserController {
    @PostMapping("/register")
    ResponseEntity<RegisterUserOutput> register(@Valid @RequestBody RegisterUserInput registerUserInput);
}
