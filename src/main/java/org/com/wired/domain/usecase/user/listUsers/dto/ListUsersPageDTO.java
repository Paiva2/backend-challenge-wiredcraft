package org.com.wired.domain.usecase.user.listUsers.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.com.wired.adapters.utils.DateUtilsAdapter;
import org.com.wired.domain.entity.User;
import org.com.wired.domain.ports.outbound.utils.DateUtilsPort;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class ListUsersPageDTO {
    private int page;
    private int size;
    private long totalItems;
    private int totalPages;
    private boolean isLast;
    private List<UserOutput> users;

    private final static DateUtilsPort dateUtilsPort = new DateUtilsAdapter();

    public static UserOutput toUserOutput(User user) {
        return UserOutput.builder()
            .id(user.getId())
            .email(user.getEmail())
            .name(user.getName())
            .profilePictureUrl(user.getProfilePictureUrl())
            .birthDate(dateUtilsPort.dateToOutputString(user.getBirthDate()))
            .description(user.getDescription())
            .address(AddressOutput.builder()
                .id(user.getAddress().getId())
                .street(user.getAddress().getStreet())
                .number(user.getAddress().getNumber())
                .neighbourhood(user.getAddress().getNeighbourhood())
                .state(user.getAddress().getState())
                .cityName(user.getAddress().getCityName())
                .country(user.getAddress().getCountry())
                .zipCode(user.getAddress().getZipCode())
                .complement(user.getAddress().getComplement())
                .build()
            ).build();
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    @Data
    public static class UserOutput {
        private Long id;
        private String email;
        private String name;
        private String profilePictureUrl;
        private String birthDate;
        private String description;
        private AddressOutput address;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    @Data
    public static class AddressOutput {
        private Long id;
        private String street;
        private String number;
        private String neighbourhood;
        private String state;
        private String cityName;
        private String country;
        private String zipCode;
        private String complement;
    }
}
