package org.com.wired.application.gateway.input;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class UpdateProfileUserInput {
    @NotBlank
    @NotNull
    private String email;

    @Size(min = 6)
    private String password;

    @NotBlank
    @NotNull
    private String name;

    private String profilePictureUrl;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @NotNull
    private Date birthDate;

    @NotBlank
    private String description;

    @NotNull
    private AddressInput address;

    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    @Data
    public static class AddressInput {
        @NotBlank
        @NotNull
        private String street;

        @NotBlank
        @NotNull
        private String number;

        @NotBlank
        @NotNull
        private String neighbourhood;

        @NotBlank
        @NotNull
        private String state;

        @NotBlank
        @NotNull
        private String cityName;

        @NotBlank
        @NotNull
        private String country;

        @NotBlank
        @NotNull
        private String zipCode;

        private String complement;
    }
}
