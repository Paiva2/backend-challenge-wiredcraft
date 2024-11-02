package org.com.wired.domain.usecase.user.authenticateUser;

import lombok.AllArgsConstructor;
import lombok.Builder;
import org.com.wired.domain.entity.User;
import org.com.wired.domain.ports.inbound.AuthenticateUserUsecasePort;
import org.com.wired.domain.ports.outbound.infra.persistence.UserRepositoryPort;
import org.com.wired.domain.ports.outbound.utils.PasswordUtilsPort;
import org.com.wired.domain.usecase.common.exception.UserDisabledException;
import org.com.wired.domain.usecase.common.exception.UserNotFoundException;
import org.com.wired.domain.usecase.user.authenticateUser.exception.WrongCredentialsException;

@AllArgsConstructor
@Builder
public class AuthenticateUserUsecase implements AuthenticateUserUsecasePort {
    private final UserRepositoryPort userRepositoryPort;
    private final PasswordUtilsPort passwordUtilsPort;

    @Override
    public Long execute(User user) {
        User userAuth = checkUserExists(user.getEmail());
        checkUserDisabled(userAuth);
        checkPasswordMatches(user.getPassword(), userAuth.getPassword());

        return userAuth.getId();
    }

    private User checkUserExists(String userEmail) {
        return userRepositoryPort.findByEmail(userEmail).orElseThrow(UserNotFoundException::new);
    }

    private void checkUserDisabled(User user) {
        if (user.getDisabledAt() != null) {
            throw new UserDisabledException(user.getId().toString());
        }
    }

    private void checkPasswordMatches(String rawPassword, String hashedPassword) {
        boolean passwordMatches = passwordUtilsPort.matches(rawPassword, hashedPassword);

        if (!passwordMatches) {
            throw new WrongCredentialsException();
        }
    }
}
