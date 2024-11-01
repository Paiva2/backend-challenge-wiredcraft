package org.com.wired.application.gateway.controller.user;

import lombok.AllArgsConstructor;
import org.com.wired.application.gateway.input.RegisterUserInput;
import org.com.wired.application.gateway.output.RegisterUserOutput;
import org.com.wired.domain.usecase.user.registerUser.RegisterUserUsecase;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
public class UserControllerImpl implements UserController {
    private final RegisterUserUsecase registerUserUsecase;

    @Override
    public ResponseEntity<RegisterUserOutput> register(RegisterUserInput registerUserInput) {
        RegisterUserOutput output = registerUserUsecase.execute(RegisterUserInput.toDomain(registerUserInput));
        return new ResponseEntity<>(output, HttpStatus.CREATED);
    }
}
