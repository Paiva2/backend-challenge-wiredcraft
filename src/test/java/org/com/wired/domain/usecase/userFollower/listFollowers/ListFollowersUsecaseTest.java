package org.com.wired.domain.usecase.userFollower.listFollowers;

import org.com.wired.domain.entity.User;
import org.com.wired.domain.ports.outbound.infra.persistence.UserFollowerRepositoryPort;
import org.com.wired.domain.ports.outbound.infra.persistence.UserRepositoryPort;
import org.com.wired.domain.usecase.common.exception.UserDisabledException;
import org.com.wired.domain.usecase.common.exception.UserNotFoundException;
import org.com.wired.domain.usecase.userFollower.listFollowers.dto.ListUserFollowersPageDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.text.MessageFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ListFollowersUsecaseTest {
    @Mock
    private UserRepositoryPort userRepositoryPort;

    @Mock
    private UserFollowerRepositoryPort userFollowerRepositoryPort;

    @Mock
    private ListFollowersUsecase sut;

    @BeforeEach
    void setUp() {
        this.sut = ListFollowersUsecase.builder()
            .userRepositoryPort(userRepositoryPort)
            .userFollowerRepositoryPort(userFollowerRepositoryPort)
            .build();
    }

    @Test
    void shouldThrowExceptionIfUserDontExists() {
        Long userMockId = 1L;

        when(userRepositoryPort.findById(anyLong())).thenReturn(Optional.empty());

        Exception error = assertThrows(UserNotFoundException.class, () -> {
            sut.execute(userMockId, 1, 5, null, "desc");
        });

        assertEquals("User not found!", error.getMessage());
        assertEquals("UserNotFoundException", error.getClass().getSimpleName());
    }

    @Test
    void shouldThrowExceptionIfUserIsDisabled() {
        Long userMockId = 1L;

        User mockUser = mockUser(userMockId);
        mockUser.setDisabledAt(new Date());

        when(userRepositoryPort.findById(anyLong())).thenReturn(Optional.of(mockUser));

        Exception error = assertThrows(UserDisabledException.class, () -> {
            sut.execute(userMockId, 1, 5, null, "desc");
        });

        assertEquals(MessageFormat.format("User with identifier {0} is disabled!", userMockId), error.getMessage());
        assertEquals("UserDisabledException", error.getClass().getSimpleName());
    }

    @Test
    void shouldMakePageOneIfLessThanOne() {
        Long userMockId = 1L;
        int pageNumberMock = -1;

        User mockUser = mockUser(userMockId);

        when(userRepositoryPort.findById(anyLong())).thenReturn(Optional.of(mockUser));
        when(userFollowerRepositoryPort.findUserFollowers(any(), anyInt(), anyInt(), any(), any())).thenReturn(ListUserFollowersPageDTO.builder()
            .page(1)
            .size(5)
            .isLast(true)
            .total(1L)
            .totalPages(1)
            .followers(List.of())
            .build()
        );

        sut.execute(userMockId, pageNumberMock, 5, null, "DESC");

        ArgumentCaptor<Integer> pageNumberCaptor = ArgumentCaptor.forClass(int.class);

        verify(userFollowerRepositoryPort, times(1)).findUserFollowers(any(), pageNumberCaptor.capture(), anyInt(), any(), any());

        assertEquals(1, pageNumberCaptor.getValue());
    }

    @Test
    void shouldMakeSizeFiveIfLessThanFive() {
        Long userMockId = 1L;
        Integer mockPageSize = 1;

        User mockUser = mockUser(userMockId);

        when(userRepositoryPort.findById(anyLong())).thenReturn(Optional.of(mockUser));
        when(userFollowerRepositoryPort.findUserFollowers(any(), anyInt(), anyInt(), any(), any())).thenReturn(ListUserFollowersPageDTO.builder()
            .page(1)
            .size(5)
            .isLast(true)
            .total(1L)
            .totalPages(1)
            .followers(List.of())
            .build()
        );

        sut.execute(userMockId, 1, mockPageSize, null, "DESC");

        ArgumentCaptor<Integer> sizeCaptor = ArgumentCaptor.forClass(int.class);

        verify(userFollowerRepositoryPort, times(1)).findUserFollowers(any(), anyInt(), sizeCaptor.capture(), any(), any());

        assertEquals(5, sizeCaptor.getValue());
    }

    @Test
    void shouldMakeSizeFifthyIfMoreThanFifthy() {
        Long userMockId = 1L;
        Integer mockPageSize = 51;

        User mockUser = mockUser(userMockId);

        when(userRepositoryPort.findById(anyLong())).thenReturn(Optional.of(mockUser));
        when(userFollowerRepositoryPort.findUserFollowers(any(), anyInt(), anyInt(), any(), any())).thenReturn(ListUserFollowersPageDTO.builder()
            .page(1)
            .size(50)
            .isLast(true)
            .total(1L)
            .totalPages(1)
            .followers(List.of())
            .build()
        );

        sut.execute(userMockId, 1, mockPageSize, null, "DESC");

        ArgumentCaptor<Integer> sizeCaptor = ArgumentCaptor.forClass(int.class);

        verify(userFollowerRepositoryPort, times(1)).findUserFollowers(any(), anyInt(), sizeCaptor.capture(), any(), any());

        assertEquals(50, sizeCaptor.getValue());
    }

    @Test
    void shouldReturnSutOutput() {
        Long userMockId = 1L;
        int pageProvided = 1;
        int sizeProvided = 5;

        User mockUser = mockUser(userMockId);

        when(userRepositoryPort.findById(anyLong())).thenReturn(Optional.of(mockUser));
        when(userFollowerRepositoryPort.findUserFollowers(any(), anyInt(), anyInt(), any(), any())).thenReturn(ListUserFollowersPageDTO.builder()
            .page(1)
            .size(5)
            .isLast(true)
            .total(1L)
            .totalPages(1)
            .followers(List.of(ListUserFollowersPageDTO.UserFollowersDto.builder()
                .followerId(2L)
                .name("any_name")
                .email("any_email")
                .followedAt(new Date().toString())
                .build()
            )).build()
        );

        ListUserFollowersPageDTO output = sut.execute(userMockId, pageProvided, sizeProvided, null, "DESC");

        assertEquals(pageProvided, output.getPage());
        assertEquals(sizeProvided, output.getSize());
        assertEquals(1L, output.getTotal());
        assertEquals(1, output.getTotalPages());
        assertTrue(output.isLast());
        assertFalse(output.getFollowers().isEmpty());
    }

    private User mockUser(Long id) {
        return User.builder()
            .id(id)
            .build();
    }
}