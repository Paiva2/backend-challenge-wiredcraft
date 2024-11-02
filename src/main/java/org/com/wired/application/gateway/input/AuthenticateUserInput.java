package org.com.wired.application.gateway.input;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.com.wired.domain.entity.User;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class AuthenticateUserInput {
    @NotBlank
    @NotNull
    private String email;

    @NotBlank
    @NotNull
    @Size(min = 6)
    private String password;

    public static User toDomain(AuthenticateUserInput input) {
        return User.builder()
            .email(input.getEmail())
            .password(input.getPassword())
            .build();
    }
}
