package org.com.wired.domain.usecase.user.findUserUsecase;

import org.com.wired.domain.entity.User;
import org.com.wired.domain.ports.outbound.infra.persistence.UserRepositoryPort;
import org.com.wired.domain.usecase.common.exception.UserDisabledException;
import org.com.wired.domain.usecase.common.exception.UserNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.text.MessageFormat;
import java.util.Date;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FindUserUsecaseTest {
    @Mock
    private UserRepositoryPort userRepositoryPort;

    private FindUserUsecase sut;

    @BeforeEach
    void setUp() {
        this.sut = FindUserUsecase.builder()
            .userRepositoryPort(userRepositoryPort)
            .build();
    }

    @Test
    void shouldThrowExceptionIfUserNotFound() {
        when(userRepositoryPort.findById(anyLong())).thenReturn(Optional.empty());

        Exception exception = assertThrows(UserNotFoundException.class, () -> {
            sut.execute(1L);
        });

        assertEquals("User not found!", exception.getMessage());
        assertEquals("UserNotFoundException", exception.getClass().getSimpleName());
    }

    @Test
    void shouldThrowExceptionIfUserDisabled() {
        User userMock = userMock(1L, "any_name", "any_email");
        userMock.setDisabledAt(new Date());

        when(userRepositoryPort.findById(anyLong())).thenReturn(Optional.of(userMock));

        Exception exception = assertThrows(UserDisabledException.class, () -> {
            sut.execute(1L);
        });

        assertEquals(MessageFormat.format("User with identifier {0} is disabled!", userMock.getId()), exception.getMessage());
        assertEquals("UserDisabledException", exception.getClass().getSimpleName());
    }

    @Test
    void shouldReturnUser() {
        User userMock = userMock(1L, "any_name", "any_email");

        when(userRepositoryPort.findById(anyLong())).thenReturn(Optional.of(userMock));

        User user = sut.execute(1L);

        assertNotNull(user);
        assertEquals(userMock.getId(), user.getId());
        assertEquals(userMock.getName(), user.getName());
        assertEquals(userMock.getEmail(), user.getEmail());
    }

    private User userMock(Long id, String name, String email) {
        return User.builder()
            .id(id)
            .name(name)
            .email(email)
            .build();
    }

}