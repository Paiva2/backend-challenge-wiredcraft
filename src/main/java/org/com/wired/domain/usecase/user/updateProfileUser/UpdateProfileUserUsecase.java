package org.com.wired.domain.usecase.user.updateProfileUser;

import lombok.AllArgsConstructor;
import lombok.Builder;
import org.com.wired.application.gateway.output.UpdateProfileUserOutput;
import org.com.wired.domain.entity.Address;
import org.com.wired.domain.entity.User;
import org.com.wired.domain.ports.inbound.usecase.UpdateProfileUserUsecasePort;
import org.com.wired.domain.ports.outbound.infra.persistence.AddressRepositoryPort;
import org.com.wired.domain.ports.outbound.infra.persistence.UserRepositoryPort;
import org.com.wired.domain.ports.outbound.strategy.EmailValidatorStrategyPort;
import org.com.wired.domain.ports.outbound.utils.PasswordUtilsPort;
import org.com.wired.domain.usecase.common.exception.AddressNotFoundException;
import org.com.wired.domain.usecase.common.exception.InvalidPropertyException;
import org.com.wired.domain.usecase.user.findUserUsecase.FindUserUsecase;
import org.com.wired.domain.usecase.user.registerUser.exception.EmailAlreadyInUseException;

import java.util.Optional;

@AllArgsConstructor
@Builder
public class UpdateProfileUserUsecase implements UpdateProfileUserUsecasePort {
    private final UserRepositoryPort userRepositoryPort;
    private final AddressRepositoryPort addressRepositoryPort;
    private final EmailValidatorStrategyPort emailValidatorStrategyPort;
    private final PasswordUtilsPort passwordUtilsPort;

    private final FindUserUsecase findUserUsecase;

    @Override
    public UpdateProfileUserOutput execute(Long userId, User input) {
        User user = findUserUsecase.execute(userId);

        Address address = findAddress(user.getId());

        if (!input.getEmail().equals(user.getEmail())) {
            checkEmailValid(input.getEmail());
            checkEmailAlreadyExists(input.getEmail(), user);
        }

        if (input.getPassword() != null) {
            checkPasswordValid(input);
            hashNewPassword(input);
        }

        fillUserUpdated(user, input);
        fillAddressUpdated(address, input.getAddress());

        user.setAddress(address);
        User userUpdated = persistUserUpdate(user);

        userUpdated.setAddress(address);

        return mountOutput(userUpdated);
    }

    private Address findAddress(Long userId) {
        return addressRepositoryPort.findByUserId(userId).orElseThrow(AddressNotFoundException::new);
    }

    private void checkEmailValid(String email) {
        boolean isEmailValid = !emailValidatorStrategyPort.execute(email);

        if (isEmailValid) {
            throw new InvalidPropertyException("Invalid e-mail.");
        }
    }

    private void checkEmailAlreadyExists(String email, User me) {
        Optional<User> user = userRepositoryPort.findByEmail(email);

        if (user.isEmpty()) return;

        boolean isUserFoundMe = user.get().getId().equals(me.getId());

        if (!isUserFoundMe) {
            throw new EmailAlreadyInUseException(email);
        }
    }

    private void checkPasswordValid(User input) {
        if (!passwordUtilsPort.hasNecessaryLength(input.getPassword())) {
            throw new InvalidPropertyException("Password must have at least 6 characters.");
        }
    }

    private void hashNewPassword(User input) {
        String hashedPassword = passwordUtilsPort.encode(input.getPassword());
        input.setPassword(hashedPassword);
    }

    private void fillUserUpdated(User user, User input) {
        user.setEmail(input.getEmail());
        user.setName(input.getName());
        user.setBirthDate(input.getBirthDate());
        user.setProfilePictureUrl(input.getProfilePictureUrl());
        user.setDescription(input.getDescription());

        if (input.getPassword() != null) {
            user.setPassword(input.getPassword());
        }
    }

    private User persistUserUpdate(User user) {
        return userRepositoryPort.persist(user);
    }

    private void fillAddressUpdated(Address address, Address addressInput) {
        address.setStreet(addressInput.getStreet());
        address.setState(addressInput.getState());
        address.setCityName(addressInput.getCityName());
        address.setZipCode(addressInput.getZipCode());
        address.setNumber(addressInput.getNumber());
        address.setCountry(addressInput.getCountry());
        address.setNeighbourhood(addressInput.getNeighbourhood());
        address.setComplement(addressInput.getComplement());
    }

    private UpdateProfileUserOutput mountOutput(User user) {
        return UpdateProfileUserOutput.output(user);
    }
}
