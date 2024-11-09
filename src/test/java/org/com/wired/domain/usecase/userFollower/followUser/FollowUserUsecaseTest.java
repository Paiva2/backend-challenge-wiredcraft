package org.com.wired.domain.usecase.userFollower.followUser;

import org.com.wired.adapters.utils.DateUtilsAdapter;
import org.com.wired.application.gateway.output.FollowUserOutput;
import org.com.wired.domain.entity.Follower;
import org.com.wired.domain.entity.User;
import org.com.wired.domain.entity.UserFollower;
import org.com.wired.domain.ports.outbound.infra.persistence.FollowerRepositoryPort;
import org.com.wired.domain.ports.outbound.infra.persistence.UserFollowerRepositoryPort;
import org.com.wired.domain.usecase.user.findUserUsecase.FindUserUsecase;
import org.com.wired.domain.usecase.userFollower.followUser.exception.InvalidFollowException;
import org.com.wired.domain.usecase.userFollower.followUser.exception.UserAlreadyFollowingException;
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
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FollowUserUsecaseTest {
    @Mock
    private FindUserUsecase findUserUsecase;

    @Mock
    private UserFollowerRepositoryPort userFollowerRepositoryPort;

    @Mock
    private FollowerRepositoryPort followerRepositoryPort;

    private FollowUserUsecase sut;

    @BeforeEach
    void setUp() {
        this.sut = FollowUserUsecase.builder()
            .findUserUsecase(findUserUsecase)
            .userFollowerRepositoryPort(userFollowerRepositoryPort)
            .followerRepositoryPort(followerRepositoryPort)
            .build();
    }

    @Test
    void shouldThrowExceptionIfUserToFollowIsSameAsUser() {
        Long mockSutUserInput = 1L;
        Long mockSutUserToFollowInput = 1L;

        User userMock = mockUser(
            1L,
            "any_name",
            "any_email",
            "any_profile_pic",
            new Date(),
            "any_description"
        );
        when(findUserUsecase.execute(mockSutUserInput)).thenReturn(userMock);
        when(findUserUsecase.execute(mockSutUserToFollowInput)).thenReturn(userMock);

        Exception error = assertThrows(InvalidFollowException.class, () -> {
            sut.execute(mockSutUserInput, mockSutUserToFollowInput);
        });

        assertEquals("Invalid follow. Users can't follow themselves.", error.getMessage());
        assertEquals("InvalidFollowException", error.getClass().getSimpleName());
    }

    @Test
    void shouldThrowExceptionIfUserAlreadyFollowsUserToFollow() {
        Long mockSutUserInput = 1L;
        Long mockSutUserToFollowInput = 2L;

        User userMock = mockUser(
            1L,
            "any_name",
            "any_email",
            "any_profile_pic",
            new Date(),
            "any_description"
        );
        when(findUserUsecase.execute(mockSutUserInput)).thenReturn(userMock);

        User userToFollowMock = mockUser(
            2L,
            "any_name",
            "any_email",
            "any_profile_pic",
            new Date(),
            "any_description"
        );
        when(findUserUsecase.execute(mockSutUserToFollowInput)).thenReturn(userToFollowMock);

        UserFollower mockUserFollower = mockUserFollower(userMock, userToFollowMock);
        when(userFollowerRepositoryPort.findUserFollowing(anyLong(), anyLong())).thenReturn(Optional.of(mockUserFollower));

        Exception error = assertThrows(UserAlreadyFollowingException.class, () -> {
            sut.execute(mockSutUserInput, mockSutUserToFollowInput);
        });

        assertEquals(MessageFormat.format("User {0} is already following user {1}.", userMock.getId(), userToFollowMock.getId()), error.getMessage());
        assertEquals("UserAlreadyFollowingException", error.getClass().getSimpleName());
    }

    @Test
    void shouldFillAndPersistFollowerCorrectly() {
        Long mockSutUserInput = 1L;
        Long mockSutUserToFollowInput = 2L;

        User userMock = mockUser(
            1L,
            "any_name",
            "any_email",
            "any_profile_pic",
            new Date(),
            "any_description"
        );
        when(findUserUsecase.execute(mockSutUserInput)).thenReturn(userMock);

        User userToFollowMock = mockUser(
            2L,
            "any_name",
            "any_email",
            "any_profile_pic",
            new Date(),
            "any_description"
        );
        when(findUserUsecase.execute(mockSutUserToFollowInput)).thenReturn(userToFollowMock);

        when(userFollowerRepositoryPort.findUserFollowing(anyLong(), anyLong())).thenReturn(Optional.empty());

        Follower followerMock = mockFollower(userMock);
        when(followerRepositoryPort.persist(any(Follower.class))).thenReturn(followerMock);

        UserFollower userFollowerMock = mockUserFollower(userMock, userToFollowMock);
        when(userFollowerRepositoryPort.persist(any(UserFollower.class))).thenReturn(userFollowerMock);

        sut.execute(mockSutUserInput, mockSutUserToFollowInput);

        ArgumentCaptor<Follower> followerArgumentCaptor = ArgumentCaptor.forClass(Follower.class);

        verify(followerRepositoryPort, times(1)).persist(followerArgumentCaptor.capture());
        assertEquals(userMock.getId(), followerArgumentCaptor.getValue().getUser().getId());
    }

    @Test
    void shouldFillAndPersistUserFollowerCorrectly() {
        Long mockSutUserInput = 1L;
        Long mockSutUserToFollowInput = 2L;

        User userMock = mockUser(
            1L,
            "any_name",
            "any_email",
            "any_profile_pic",
            new Date(),
            "any_description"
        );
        when(findUserUsecase.execute(mockSutUserInput)).thenReturn(userMock);

        User userToFollowMock = mockUser(
            2L,
            "any_name",
            "any_email",
            "any_profile_pic",
            new Date(),
            "any_description"
        );
        when(findUserUsecase.execute(mockSutUserToFollowInput)).thenReturn(userToFollowMock);

        when(userFollowerRepositoryPort.findUserFollowing(anyLong(), anyLong())).thenReturn(Optional.empty());

        Follower followerMock = mockFollower(userMock);
        when(followerRepositoryPort.persist(any(Follower.class))).thenReturn(followerMock);

        UserFollower userFollowerMock = mockUserFollower(userMock, userToFollowMock);
        when(userFollowerRepositoryPort.persist(any(UserFollower.class))).thenReturn(userFollowerMock);

        sut.execute(mockSutUserInput, mockSutUserToFollowInput);

        ArgumentCaptor<UserFollower> userFollowerArgumentCaptor = ArgumentCaptor.forClass(UserFollower.class);

        verify(userFollowerRepositoryPort, times(1)).persist(userFollowerArgumentCaptor.capture());
        assertEquals(userToFollowMock.getId(), userFollowerArgumentCaptor.getValue().getUser().getId());
        assertEquals(userMock.getId(), userFollowerArgumentCaptor.getValue().getFollower().getUser().getId());
        assertNotNull(userFollowerArgumentCaptor.getValue().getKeyId());
    }

    @Test
    void shouldReturnSutOutput() {
        Long mockSutUserInput = 1L;
        Long mockSutUserToFollowInput = 2L;

        User userMock = mockUser(
            1L,
            "any_name",
            "any_email",
            "any_profile_pic",
            new Date(),
            "any_description"
        );
        when(findUserUsecase.execute(mockSutUserInput)).thenReturn(userMock);

        User userToFollowMock = mockUser(
            2L,
            "any_name",
            "any_email",
            "any_profile_pic",
            new Date(),
            "any_description"
        );
        when(findUserUsecase.execute(mockSutUserToFollowInput)).thenReturn(userToFollowMock);

        when(userFollowerRepositoryPort.findUserFollowing(anyLong(), anyLong())).thenReturn(Optional.empty());

        Follower followerMock = mockFollower(userMock);
        when(followerRepositoryPort.persist(any(Follower.class))).thenReturn(followerMock);

        UserFollower userFollowerMock = mockUserFollower(userMock, userToFollowMock);
        when(userFollowerRepositoryPort.persist(any(UserFollower.class))).thenReturn(userFollowerMock);

        FollowUserOutput output = sut.execute(mockSutUserInput, mockSutUserToFollowInput);

        assertNotNull(output);
        assertEquals(output.getUserFollowedId(), userToFollowMock.getId());
        assertEquals(output.getFollowedAt(), formatDateOutput(new Date()));
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

    private UserFollower mockUserFollower(User user, User userFollowing) {
        return UserFollower.builder()
            .keyId(UserFollower.KeyId.builder()
                .userId(user.getId())
                .followerId(1L)
                .build()
            ).user(user)
            .follower(Follower.builder()
                .id(1L)
                .user(userFollowing)
                .build()
            ).build();
    }

    private Follower mockFollower(User userFollowing) {
        return Follower.builder()
            .id(1L)
            .user(userFollowing)
            .build();
    }

    private String formatDateOutput(Date date) {
        DateUtilsAdapter dateUtilsAdapter = new DateUtilsAdapter();
        return dateUtilsAdapter.dateToOutputString(date);
    }
}