package org.com.wired.application.gateway.controller.user;

import lombok.AllArgsConstructor;
import org.com.wired.adapters.strategy.infra.auth.AuthenticationStrategyConcrete;
import org.com.wired.application.gateway.input.AuthenticateUserInput;
import org.com.wired.application.gateway.input.RegisterUserInput;
import org.com.wired.application.gateway.output.RegisterUserOutput;
import org.com.wired.domain.usecase.user.authenticateUser.AuthenticateUserUsecase;
import org.com.wired.domain.usecase.user.registerUser.RegisterUserUsecase;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.Map;

@AllArgsConstructor
@RestController
public class UserControllerImpl implements UserController {
    private final RegisterUserUsecase registerUserUsecase;
    private final AuthenticateUserUsecase authenticateUserUsecase;
    private final AuthenticationStrategyConcrete authenticationStrategyConcrete;

    @Override
    public ResponseEntity<RegisterUserOutput> register(RegisterUserInput registerUserInput) {
        RegisterUserOutput output = registerUserUsecase.execute(RegisterUserInput.toDomain(registerUserInput));
        return new ResponseEntity<>(output, HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<Map<String, String>> login(AuthenticateUserInput input) {
        Long authenticate = authenticateUserUsecase.execute(AuthenticateUserInput.toDomain(input));
        String token = authenticationStrategyConcrete.createAuthentication(authenticate.toString());
        return new ResponseEntity<>(Collections.singletonMap("token", token), HttpStatus.OK);
    }
}
