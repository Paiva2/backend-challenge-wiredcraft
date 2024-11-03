package org.com.wired.application.gateway.output;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.com.wired.adapters.utils.DateUtilsAdapter;
import org.com.wired.domain.entity.Address;
import org.com.wired.domain.entity.User;
import org.com.wired.domain.ports.outbound.utils.DateUtilsPort;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class UpdateProfileUserOutput {
    private Long id;
    private String name;
    private String email;
    private String profilePictureUrl;
    private String birthDate;
    private String description;
    private AddressOutput address;

    private static final DateUtilsPort dateUtilsPort = new DateUtilsAdapter();

    public static UpdateProfileUserOutput output(User user) {
        Address address = user.getAddress();

        return UpdateProfileUserOutput.builder()
            .id(user.getId())
            .name(user.getName())
            .email(user.getEmail())
            .profilePictureUrl(user.getProfilePictureUrl())
            .birthDate(dateUtilsPort.dateToOutputString(user.getBirthDate()))
            .description(user.getDescription())
            .address(UpdateProfileUserOutput.AddressOutput.builder()
                .street(address.getStreet())
                .number(address.getNumber())
                .neighbourhood(address.getNeighbourhood())
                .state(address.getState())
                .cityName(address.getCityName())
                .country(address.getCountry())
                .zipCode(address.getZipCode())
                .complement(address.getComplement())
                .updatedAt(address.getUpdatedAt())
                .build()
            ).build();
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    @Data
    public static class AddressOutput {
        private String street;
        private String number;
        private String neighbourhood;
        private String state;
        private String cityName;
        private String country;
        private String zipCode;
        private String complement;
        private Date updatedAt;
    }
}
