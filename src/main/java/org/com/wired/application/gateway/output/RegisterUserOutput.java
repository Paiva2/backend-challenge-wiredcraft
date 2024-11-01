package org.com.wired.application.gateway.output;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.com.wired.adapters.utils.DateUtilsAdapter;
import org.com.wired.domain.entity.User;
import org.com.wired.domain.ports.outbound.utils.DateUtilsPort;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class RegisterUserOutput {
    private Long id;
    private String name;
    private String email;
    private String profilePictureUrl;
    private String birthDate;
    private String description;

    private static final DateUtilsPort dateUtilsPort = new DateUtilsAdapter();

    public static RegisterUserOutput output(User user) {
        return RegisterUserOutput.builder()
            .id(user.getId())
            .name(user.getName())
            .email(user.getEmail())
            .profilePictureUrl(user.getProfilePictureUrl())
            .birthDate(dateUtilsPort.dateToOutputString(user.getBirthDate()))
            .description(user.getDescription())
            .build();
    }
}
