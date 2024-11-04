package org.com.wired.domain.usecase.user.disableAccount;

import org.com.wired.domain.entity.User;
import org.com.wired.domain.ports.outbound.infra.persistence.UserRepositoryPort;
import org.com.wired.domain.usecase.common.exception.UserDisabledException;
import org.com.wired.domain.usecase.common.exception.UserNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.text.MessageFormat;
import java.util.Date;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DisableAccountUsecaseTest {
    @Mock
    private UserRepositoryPort userRepositoryPort;

    private DisableAccountUsecase sut;

    @BeforeEach
    void setUp() {
        this.sut = DisableAccountUsecase.builder()
            .userRepositoryPort(userRepositoryPort)
            .build();
    }

    @Test
    void shouldThrowExceptionIfUserNotFound() {
        Long mockSutInput = 1L;

        when(userRepositoryPort.findById(mockSutInput)).thenReturn(Optional.empty());

        Exception error = assertThrows(UserNotFoundException.class, () -> {
            sut.execute(mockSutInput);
        });

        assertEquals("User not found!", error.getMessage());
        assertEquals("UserNotFoundException", error.getClass().getSimpleName());
    }

    @Test
    void shouldThrowExceptionIfUserIsDisabled() {
        Long mockSutInput = 1L;

        User userMock = mockUser(
            1L,
            "any_name",
            "any_email",
            "any_profile_pic",
            new Date(),
            "any_description"
        );
        userMock.setDisabledAt(new Date());

        when(userRepositoryPort.findById(mockSutInput)).thenReturn(Optional.of(userMock));

        Exception error = assertThrows(UserDisabledException.class, () -> {
            sut.execute(mockSutInput);
        });

        assertEquals(MessageFormat.format("User with identifier {0} is disabled!", userMock.getId()), error.getMessage());
        assertEquals("UserDisabledException", error.getClass().getSimpleName());
    }

    @Test
    void shouldPersistUserDisabledWithoutProblems() {
        Long mockSutInput = 1L;

        User userMock = mockUser(
            1L,
            "any_name",
            "any_email",
            "any_profile_pic",
            new Date(),
            "any_description"
        );

        when(userRepositoryPort.findById(mockSutInput)).thenReturn(Optional.of(userMock));

        sut.execute(mockSutInput);

        ArgumentCaptor<User> userArgumentCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepositoryPort, times(1)).persist(userArgumentCaptor.capture());

        assertNotNull(userArgumentCaptor.getValue().getDisabledAt());
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