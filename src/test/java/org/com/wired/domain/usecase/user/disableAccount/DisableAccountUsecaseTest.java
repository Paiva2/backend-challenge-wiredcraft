package org.com.wired.domain.usecase.user.disableAccount;

import org.com.wired.domain.entity.User;
import org.com.wired.domain.ports.outbound.infra.persistence.UserRepositoryPort;
import org.com.wired.domain.usecase.user.findUserUsecase.FindUserUsecase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DisableAccountUsecaseTest {
    @Mock
    private UserRepositoryPort userRepositoryPort;

    @Mock
    private FindUserUsecase findUserUsecase;

    private DisableAccountUsecase sut;

    @BeforeEach
    void setUp() {
        this.sut = DisableAccountUsecase.builder()
            .userRepositoryPort(userRepositoryPort)
            .findUserUsecase(findUserUsecase)
            .build();
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

        when(findUserUsecase.execute(mockSutInput)).thenReturn(userMock);

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