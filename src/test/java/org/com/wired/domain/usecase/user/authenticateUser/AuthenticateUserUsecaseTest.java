package org.com.wired.domain.usecase.user.authenticateUser;

import org.com.wired.domain.entity.User;
import org.com.wired.domain.ports.outbound.infra.persistence.UserRepositoryPort;
import org.com.wired.domain.ports.outbound.utils.PasswordUtilsPort;
import org.com.wired.domain.usecase.common.exception.UserDisabledException;
import org.com.wired.domain.usecase.common.exception.UserNotFoundException;
import org.com.wired.domain.usecase.user.authenticateUser.exception.WrongCredentialsException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.text.MessageFormat;
import java.util.Date;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthenticateUserUsecaseTest {
    @Mock
    private UserRepositoryPort userRepositoryPort;

    @Mock
    private PasswordUtilsPort passwordUtilsPort;

    private AuthenticateUserUsecase sut;

    @BeforeEach
    void setUp() {
        this.sut = AuthenticateUserUsecase.builder()
            .userRepositoryPort(userRepositoryPort)
            .passwordUtilsPort(passwordUtilsPort)
            .build();
    }

    @Test
    void shouldThrowExceptionIfUserNotFound() {
        User mockSutInput = mockInput();

        when(userRepositoryPort.findByEmail(mockSutInput.getEmail())).thenReturn(Optional.empty());

        Exception error = assertThrows(UserNotFoundException.class, () -> {
            sut.execute(mockSutInput);
        });

        assertEquals("User not found!", error.getMessage());
        assertEquals("UserNotFoundException", error.getClass().getSimpleName());
    }

    @Test
    void shouldThrowExceptionIfUserIsDisabled() {
        User mockSutInput = mockInput();
        User userMock = mockUser(
            1L,
            mockSutInput.getName(),
            mockSutInput.getEmail(),
            mockSutInput.getProfilePictureUrl(),
            mockSutInput.getBirthDate(),
            mockSutInput.getDescription()
        );
        userMock.setDisabledAt(new Date());

        when(userRepositoryPort.findByEmail(mockSutInput.getEmail())).thenReturn(Optional.of(userMock));

        Exception error = assertThrows(UserDisabledException.class, () -> {
            sut.execute(mockSutInput);
        });

        assertEquals(MessageFormat.format("User with identifier {0} is disabled!", userMock.getId()), error.getMessage());
        assertEquals("UserDisabledException", error.getClass().getSimpleName());
    }

    @Test
    void shouldThrowExceptionIfPasswordDontMatch() {
        User mockSutInput = mockInput();
        User userMock = mockUser(
            1L,
            mockSutInput.getName(),
            mockSutInput.getEmail(),
            mockSutInput.getProfilePictureUrl(),
            mockSutInput.getBirthDate(),
            mockSutInput.getDescription()
        );

        when(userRepositoryPort.findByEmail(mockSutInput.getEmail())).thenReturn(Optional.of(userMock));
        when(passwordUtilsPort.matches(anyString(), anyString())).thenReturn(false);

        Exception error = assertThrows(WrongCredentialsException.class, () -> {
            sut.execute(mockSutInput);
        });

        assertEquals("Wrong credentials!", error.getMessage());
        assertEquals("WrongCredentialsException", error.getClass().getSimpleName());
    }

    @Test
    void shouldReturnSutOutput() {
        User mockSutInput = mockInput();
        User userMock = mockUser(
            1L,
            mockSutInput.getName(),
            mockSutInput.getEmail(),
            mockSutInput.getProfilePictureUrl(),
            mockSutInput.getBirthDate(),
            mockSutInput.getDescription()
        );

        when(userRepositoryPort.findByEmail(mockSutInput.getEmail())).thenReturn(Optional.of(userMock));
        when(passwordUtilsPort.matches(anyString(), anyString())).thenReturn(true);

        Long output = sut.execute(mockSutInput);

        assertEquals(userMock.getId(), output);
    }

    private User mockInput() {
        return User.builder()
            .email("any_email")
            .password("any_password")
            .build();
    }

    private User mockUser(Long id, String name, String email, String profilePic, Date birthDate, String description) {
        return User.builder()
            .id(id)
            .name(name)
            .email(email)
            .profilePictureUrl(profilePic)
            .birthDate(birthDate)
            .description(description)
            .password("hashed_password")
            .build();
    }
}