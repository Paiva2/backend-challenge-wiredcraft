package org.com.wired.domain.usecase.user.updateProfileUser;

import org.com.wired.adapters.utils.DateUtilsAdapter;
import org.com.wired.application.gateway.output.UpdateProfileUserOutput;
import org.com.wired.domain.entity.Address;
import org.com.wired.domain.entity.User;
import org.com.wired.domain.ports.outbound.infra.persistence.AddressRepositoryPort;
import org.com.wired.domain.ports.outbound.infra.persistence.UserRepositoryPort;
import org.com.wired.domain.ports.outbound.strategy.EmailValidatorStrategyPort;
import org.com.wired.domain.ports.outbound.utils.PasswordUtilsPort;
import org.com.wired.domain.usecase.common.exception.AddressNotFoundException;
import org.com.wired.domain.usecase.common.exception.InvalidPropertyException;
import org.com.wired.domain.usecase.common.exception.UserDisabledException;
import org.com.wired.domain.usecase.common.exception.UserNotFoundException;
import org.com.wired.domain.usecase.user.registerUser.exception.EmailAlreadyInUseException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.text.MessageFormat;
import java.util.Date;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UpdateProfileUserUsecaseTest {
    @Mock
    private UserRepositoryPort userRepositoryPort;

    @Mock
    private AddressRepositoryPort addressRepositoryPort;

    @Mock
    private EmailValidatorStrategyPort emailValidatorStrategyPort;

    @Mock
    private PasswordUtilsPort passwordUtilsPort;

    private UpdateProfileUserUsecase sut;

    @BeforeEach
    void setUp() {
        this.sut = UpdateProfileUserUsecase.builder()
            .userRepositoryPort(userRepositoryPort)
            .addressRepositoryPort(addressRepositoryPort)
            .emailValidatorStrategyPort(emailValidatorStrategyPort)
            .passwordUtilsPort(passwordUtilsPort)
            .build();
    }

    @Test
    void shouldThrowExceptionIfUserNotFound() {
        Long mockUserId = 1L;
        User mockSutInput = mockSutInput(mockUserId, "any_email", null);

        when(userRepositoryPort.findById(mockUserId)).thenReturn(Optional.empty());

        Exception error = assertThrows(UserNotFoundException.class, () -> {
            sut.execute(1L, mockSutInput);
        });

        assertEquals("User not found!", error.getMessage());
        assertEquals("UserNotFoundException", error.getClass().getSimpleName());
    }

    @Test
    void shouldThrowExceptionIfUserDisabled() {
        Long mockUserId = 1L;
        User mockSutInput = mockSutInput(mockUserId, "any_email", null);
        User mockUser = mockUser(mockUserId, mockSutInput.getEmail(), new Date());

        when(userRepositoryPort.findById(mockUserId)).thenReturn(Optional.of(mockUser));

        Exception error = assertThrows(UserDisabledException.class, () -> {
            sut.execute(1L, mockSutInput);
        });

        assertEquals(MessageFormat.format("User with identifier {0} is disabled!", mockUserId), error.getMessage());
        assertEquals("UserDisabledException", error.getClass().getSimpleName());
    }

    @Test
    void shouldThrowExceptionIfUserAddressNotFound() {
        Long mockUserId = 1L;
        User mockSutInput = mockSutInput(mockUserId, "any_email", null);
        User mockUser = mockUser(mockUserId, mockSutInput.getEmail(), null);

        when(userRepositoryPort.findById(mockUserId)).thenReturn(Optional.of(mockUser));
        when(addressRepositoryPort.findByUserId(any())).thenReturn(Optional.empty());

        Exception error = assertThrows(AddressNotFoundException.class, () -> {
            sut.execute(1L, mockSutInput);
        });

        assertEquals("Address not found!", error.getMessage());
        assertEquals("AddressNotFoundException", error.getClass().getSimpleName());
    }

    @Test
    void shouldThrowExceptionIfNewEmailIsInvalid() {
        Long mockUserId = 1L;
        User mockSutInput = mockSutInput(mockUserId, "any_new_email", null);
        User mockUser = mockUser(mockUserId, "any_current_email", null);
        Address mockAddress = mockAddress();

        when(userRepositoryPort.findById(mockUserId)).thenReturn(Optional.of(mockUser));
        when(addressRepositoryPort.findByUserId(any())).thenReturn(Optional.of(mockAddress));
        when(emailValidatorStrategyPort.execute(any())).thenReturn(false);

        Exception error = assertThrows(InvalidPropertyException.class, () -> {
            sut.execute(1L, mockSutInput);
        });

        assertEquals("Invalid property. Reason: Invalid e-mail.", error.getMessage());
        assertEquals("InvalidPropertyException", error.getClass().getSimpleName());
    }

    @Test
    void shouldThrowExceptionIfNewEmailIsInUse() {
        Long mockUserId = 1L;
        User mockSutInput = mockSutInput(mockUserId, "any_new_email", null);
        User mockUser = mockUser(mockUserId, "any_current_email", null);
        Address mockAddress = mockAddress();

        when(userRepositoryPort.findById(mockUserId)).thenReturn(Optional.of(mockUser));
        when(addressRepositoryPort.findByUserId(any())).thenReturn(Optional.of(mockAddress));
        when(emailValidatorStrategyPort.execute(any())).thenReturn(true);

        User mockExistingEmailUser = User.builder().id(2L).build();
        when(userRepositoryPort.findByEmail(any())).thenReturn(Optional.of(mockExistingEmailUser));

        Exception error = assertThrows(EmailAlreadyInUseException.class, () -> {
            sut.execute(1L, mockSutInput);
        });

        assertEquals(MessageFormat.format("Email {0} is already in use.", mockSutInput.getEmail()), error.getMessage());
        assertEquals("EmailAlreadyInUseException", error.getClass().getSimpleName());
    }

    @Test
    void shouldThrowExceptionIfPasswordIsInvalid() {
        Long mockUserId = 1L;
        User mockSutInput = mockSutInput(mockUserId, "any_new_email", "any_password");
        User mockUser = mockUser(mockUserId, "any_new_email", null);
        Address mockAddress = mockAddress();

        when(userRepositoryPort.findById(mockUserId)).thenReturn(Optional.of(mockUser));
        when(addressRepositoryPort.findByUserId(any())).thenReturn(Optional.of(mockAddress));
        when(passwordUtilsPort.hasNecessaryLength(any())).thenReturn(false);

        Exception error = assertThrows(InvalidPropertyException.class, () -> {
            sut.execute(1L, mockSutInput);
        });

        assertEquals("Invalid property. Reason: Password must have at least 6 characters.", error.getMessage());
        assertEquals("InvalidPropertyException", error.getClass().getSimpleName());
    }

    @Test
    void shouldHashNewPasswordIfNewOneProvided() {
        Long mockUserId = 1L;
        User mockSutInput = mockSutInput(mockUserId, "any_new_email", "any_password");
        User mockUser = mockUser(mockUserId, "any_new_email", null);
        Address mockAddress = mockAddress();

        when(userRepositoryPort.findById(mockUserId)).thenReturn(Optional.of(mockUser));
        when(addressRepositoryPort.findByUserId(any())).thenReturn(Optional.of(mockAddress));
        when(passwordUtilsPort.hasNecessaryLength(any())).thenReturn(true);
        when(passwordUtilsPort.encode(any())).thenReturn("hashed_password");

        User mockUserPersisted = User.builder()
            .id(mockUserId)
            .name(mockSutInput.getName())
            .email(mockSutInput.getEmail())
            .profilePictureUrl(mockSutInput.getProfilePictureUrl())
            .description(mockSutInput.getDescription())
            .birthDate(mockSutInput.getBirthDate())
            .updatedAt(new Date())
            .build();
        when(userRepositoryPort.persist(any())).thenReturn(mockUserPersisted);

        sut.execute(1L, mockSutInput);

        verify(passwordUtilsPort, times(1)).encode(any());

        ArgumentCaptor<User> userFilledCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepositoryPort, times(1)).persist(userFilledCaptor.capture());

        assertEquals("hashed_password", userFilledCaptor.getValue().getPassword());
    }

    @Test
    void shouldUpdateUserAndAddressWithInputProvided() {
        Long mockUserId = 1L;
        User mockSutInput = mockSutInput(mockUserId, "any_new_email_updated", "any_password");
        mockSutInput.setName("update_name");
        mockSutInput.setDescription("update_description");
        mockSutInput.getAddress().setStreet("update_street");
        mockSutInput.getAddress().setNeighbourhood("update_neighbourhood");
        mockSutInput.getAddress().setNeighbourhood("update_state");
        mockSutInput.getAddress().setNeighbourhood("update_zip_code");

        User mockUser = mockUser(mockUserId, "any_new_email", null);
        Address mockAddress = mockAddress();

        when(userRepositoryPort.findById(mockUserId)).thenReturn(Optional.of(mockUser));
        when(addressRepositoryPort.findByUserId(any())).thenReturn(Optional.of(mockAddress));
        when(emailValidatorStrategyPort.execute(any())).thenReturn(true);
        when(passwordUtilsPort.hasNecessaryLength(any())).thenReturn(true);
        when(passwordUtilsPort.encode(any())).thenReturn("hashed_password");

        User mockUserPersisted = User.builder()
            .id(mockUserId)
            .name(mockSutInput.getName())
            .email(mockSutInput.getEmail())
            .profilePictureUrl(mockSutInput.getProfilePictureUrl())
            .description(mockSutInput.getDescription())
            .birthDate(mockSutInput.getBirthDate())
            .updatedAt(new Date())
            .build();
        when(userRepositoryPort.persist(any())).thenReturn(mockUserPersisted);

        UpdateProfileUserOutput sutOutput = sut.execute(1L, mockSutInput);

        verify(userRepositoryPort, times(1)).persist(any(User.class));
        assertEquals(mockUserId, sutOutput.getId());
        assertEquals(mockSutInput.getEmail(), sutOutput.getEmail());
        assertEquals(mockSutInput.getName(), sutOutput.getName());
        assertEquals(formatDateOutput(mockSutInput.getBirthDate()), sutOutput.getBirthDate());
        assertEquals(mockSutInput.getDescription(), sutOutput.getDescription());
        assertEquals(mockSutInput.getProfilePictureUrl(), sutOutput.getProfilePictureUrl());
        assertEquals(mockSutInput.getAddress().getStreet(), sutOutput.getAddress().getStreet());
        assertEquals(mockSutInput.getAddress().getNumber(), sutOutput.getAddress().getNumber());
        assertEquals(mockSutInput.getAddress().getNeighbourhood(), sutOutput.getAddress().getNeighbourhood());
        assertEquals(mockSutInput.getAddress().getCityName(), sutOutput.getAddress().getCityName());
        assertEquals(mockSutInput.getAddress().getState(), sutOutput.getAddress().getState());
        assertEquals(mockSutInput.getAddress().getZipCode(), sutOutput.getAddress().getZipCode());
    }

    private User mockSutInput(Long id, String email, String apssword) {
        return User.builder()
            .id(id)
            .email(email)
            .birthDate(new Date())
            .name("any_name")
            .description("any_description")
            .profilePictureUrl("any_profile_picture")
            .address(Address.builder()
                .street("any_street")
                .number("any_number")
                .neighbourhood("any_neighbourhood")
                .state("any_state")
                .cityName("any_city_name")
                .country("any_country")
                .zipCode("any_zip_code")
                .complement("any_complement")
                .build()
            )
            .password(apssword)
            .build();
    }

    private User mockUser(Long id, String email, Date disabledAt) {
        return User.builder()
            .id(id)
            .email(email)
            .birthDate(new Date())
            .name("any_name")
            .description("any_description")
            .profilePictureUrl("any_profile_picture")
            .disabledAt(disabledAt)
            .build();
    }

    private Address mockAddress() {
        return Address.builder().build();
    }

    private String formatDateOutput(Date date) {
        DateUtilsAdapter dateUtilsAdapter = new DateUtilsAdapter();
        return dateUtilsAdapter.dateToOutputString(date);
    }
}