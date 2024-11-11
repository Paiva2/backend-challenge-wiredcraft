package org.com.wired.application.gateway.controller.user;

import jakarta.validation.Valid;
import org.com.wired.application.gateway.input.AuthenticateUserInput;
import org.com.wired.application.gateway.input.RegisterUserInput;
import org.com.wired.application.gateway.input.UpdateProfileUserInput;
import org.com.wired.application.gateway.output.GetProfileUserOutput;
import org.com.wired.application.gateway.output.RegisterUserOutput;
import org.com.wired.application.gateway.output.UpdateProfileUserOutput;
import org.com.wired.domain.usecase.user.listUsers.dto.ListUsersPageDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/list")
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    ResponseEntity<ListUsersPageDTO> getUsersList(@RequestParam(value = "page", required = false, defaultValue = "1") Integer page, @RequestParam(value = "size", required = false, defaultValue = "1") Integer size, @RequestParam(value = "name", required = false) String name);

    @PatchMapping("/profile/update")
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    ResponseEntity<UpdateProfileUserOutput> updateProfile(Authentication authentication, @RequestBody @Valid UpdateProfileUserInput updateProfileUserInput);

    @DeleteMapping("/disable")
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    ResponseEntity<Void> disable(Authentication authentication);

}
