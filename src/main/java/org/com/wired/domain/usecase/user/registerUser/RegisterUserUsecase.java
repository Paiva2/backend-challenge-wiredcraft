package org.com.wired.domain.usecase.user.registerUser;

import lombok.AllArgsConstructor;
import lombok.Builder;
import org.com.wired.application.gateway.output.RegisterUserOutput;
import org.com.wired.domain.entity.Address;
import org.com.wired.domain.entity.User;
import org.com.wired.domain.ports.inbound.RegisterUserUsecasePort;
import org.com.wired.domain.ports.outbound.infra.persistence.AddressRepositoryPort;
import org.com.wired.domain.ports.outbound.infra.persistence.UserRepositoryPort;
import org.com.wired.domain.ports.outbound.strategy.EmailValidatorStrategyPort;
import org.com.wired.domain.ports.outbound.utils.PasswordUtilsPort;
import org.com.wired.domain.usecase.common.exception.InvalidPropertyException;
import org.com.wired.domain.usecase.user.registerUser.exception.EmailAlreadyInUseException;

import java.util.Optional;

// todo: implement e-mail confirmation
@AllArgsConstructor
@Builder
public class RegisterUserUsecase implements RegisterUserUsecasePort {
    private final UserRepositoryPort userRepositoryPort;
    private final AddressRepositoryPort addressRepositoryPort;

    private final EmailValidatorStrategyPort validatorStrategyPort;
    private final PasswordUtilsPort passwordUtilsPort;

    public RegisterUserOutput execute(User inputUser) {
        checkEmailValidFormat(inputUser.getEmail());
        checkEmailBeingUsed(inputUser.getEmail());
        checkPasswordSize(inputUser.getPassword());
        hashPassword(inputUser);

        User newUser = fillUser(inputUser);
        newUser = persistUser(newUser);

        inputUser.getAddress().setUser(newUser);
        persistAddress(inputUser.getAddress());

        return mountOutput(newUser);
    }

    private void checkEmailBeingUsed(String email) {
        Optional<User> findUserEmail = userRepositoryPort.findByEmail(email);

        if (findUserEmail.isPresent()) {
            throw new EmailAlreadyInUseException(email);
        }
    }

    private void checkEmailValidFormat(String email) {
        if (!validatorStrategyPort.execute(email)) {
            throw new InvalidPropertyException("Invalid e-mail.");
        }
    }

    private void checkPasswordSize(String password) {
        if (password.length() < 6) {
            throw new InvalidPropertyException("Password must have at least 6 characters.");
        }
    }

    private void hashPassword(User inputUser) {
        String hashedPassword = passwordUtilsPort.encode(inputUser.getPassword());
        inputUser.setPassword(hashedPassword);
    }

    private User fillUser(User inputUser) {
        return User.builder()
            .email(inputUser.getEmail())
            .password(inputUser.getPassword())
            .name(inputUser.getName())
            .profilePictureUrl(inputUser.getProfilePictureUrl())
            .birthDate(inputUser.getBirthDate())
            .description(inputUser.getDescription())
            .build();
    }

    private User persistUser(User user) {
        return userRepositoryPort.persist(user);
    }

    private void persistAddress(Address address) {
        addressRepositoryPort.persist(address);
    }

    private RegisterUserOutput mountOutput(User user) {
        return RegisterUserOutput.output(user);
    }
}
