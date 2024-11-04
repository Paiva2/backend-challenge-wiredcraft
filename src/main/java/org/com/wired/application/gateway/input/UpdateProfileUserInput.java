package org.com.wired.application.gateway.input;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.com.wired.domain.entity.Address;
import org.com.wired.domain.entity.User;
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

    public static User toDomain(UpdateProfileUserInput input) {
        return User.builder()
            .email(input.getEmail())
            .password(input.getPassword())
            .name(input.getName())
            .profilePictureUrl(input.getProfilePictureUrl())
            .birthDate(input.getBirthDate())
            .description(input.getDescription())
            .address(Address.builder()
                .street(input.getAddress().getStreet())
                .number(input.getAddress().getNumber())
                .neighbourhood(input.getAddress().getNeighbourhood())
                .state(input.getAddress().getState())
                .cityName(input.getAddress().getCityName())
                .country(input.getAddress().getCountry())
                .zipCode(input.getAddress().getZipCode())
                .complement(input.getAddress().getComplement())
                .build()
            ).build();
    }
}
