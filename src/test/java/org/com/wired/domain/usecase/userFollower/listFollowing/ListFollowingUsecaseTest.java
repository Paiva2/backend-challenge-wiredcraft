package org.com.wired.domain.usecase.userFollower.listFollowing;

import org.com.wired.domain.entity.User;
import org.com.wired.domain.ports.outbound.infra.persistence.UserFollowerRepositoryPort;
import org.com.wired.domain.usecase.user.findUserUsecase.FindUserUsecase;
import org.com.wired.domain.usecase.userFollower.listFollowing.dto.ListFollowingDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ListFollowingUsecaseTest {
    @Mock
    private FindUserUsecase findUserUsecase;

    @Mock
    private UserFollowerRepositoryPort userFollowerRepositoryPort;

    private ListFollowingUsecase sut;

    @BeforeEach
    void setUp() {
        this.sut = ListFollowingUsecase.builder()
            .findUserUsecase(findUserUsecase)
            .userFollowerRepositoryPort(userFollowerRepositoryPort)
            .build();
    }

    @Test
    void shouldMakePageOneIfLessThanOne() {
        Long userMockId = 1L;
        int pageNumberMock = -1;

        User userMock = mockUser(userMockId);

        when(findUserUsecase.execute(userMockId)).thenReturn(userMock);
        when(userFollowerRepositoryPort.listFollowing(any(), anyInt(), anyInt(), any(), any(), anyInt())).thenReturn(ListFollowingDTO.builder()
            .page(1)
            .size(5)
            .isLast(true)
            .total(1L)
            .totalPages(1)
            .followers(List.of())
            .build()
        );

        sut.execute(userMockId, pageNumberMock, 5, null, "ASC", 10);

        ArgumentCaptor<Integer> pageNumberCaptor = ArgumentCaptor.forClass(int.class);

        verify(userFollowerRepositoryPort, times(1)).listFollowing(any(), pageNumberCaptor.capture(), anyInt(), any(), any(), anyInt());

        assertEquals(1, pageNumberCaptor.getValue());
    }

    @Test
    void shouldMakeSizeFiveIfLessThanFive() {
        Long userMockId = 1L;
        Integer mockPageSize = 1;

        User userMock = mockUser(userMockId);

        when(findUserUsecase.execute(userMockId)).thenReturn(userMock);
        when(userFollowerRepositoryPort.listFollowing(any(), anyInt(), anyInt(), any(), any(), anyInt())).thenReturn(ListFollowingDTO.builder()
            .page(1)
            .size(5)
            .isLast(true)
            .total(1L)
            .totalPages(1)
            .followers(List.of())
            .build()
        );

        sut.execute(userMockId, 1, mockPageSize, null, "DESC", 10);

        ArgumentCaptor<Integer> sizeCaptor = ArgumentCaptor.forClass(int.class);

        verify(userFollowerRepositoryPort, times(1)).listFollowing(any(), anyInt(), sizeCaptor.capture(), any(), any(), anyInt());

        assertEquals(5, sizeCaptor.getValue());
    }

    @Test
    void shouldMakeSizeFifthyIfMoreThanFifthy() {
        Long userMockId = 1L;
        Integer mockPageSize = 51;

        User userMock = mockUser(userMockId);

        when(findUserUsecase.execute(userMockId)).thenReturn(userMock);
        when(userFollowerRepositoryPort.listFollowing(any(), anyInt(), anyInt(), any(), any(), anyInt())).thenReturn(ListFollowingDTO.builder()
            .page(1)
            .size(5)
            .isLast(true)
            .total(1L)
            .totalPages(1)
            .followers(List.of())
            .build()
        );

        sut.execute(userMockId, 1, mockPageSize, null, "DESC", 10);

        ArgumentCaptor<Integer> sizeCaptor = ArgumentCaptor.forClass(int.class);

        verify(userFollowerRepositoryPort, times(1)).listFollowing(any(), anyInt(), sizeCaptor.capture(), any(), any(), anyInt());

        assertEquals(50, sizeCaptor.getValue());
    }

    @Test
    void shouldReturnSutOutput() {
        Long userMockId = 1L;
        int pageProvided = 1;
        int sizeProvided = 5;

        User userMock = mockUser(userMockId);

        when(findUserUsecase.execute(userMockId)).thenReturn(userMock);
        when(userFollowerRepositoryPort.listFollowing(any(), anyInt(), anyInt(), any(), any(), anyInt())).thenReturn(ListFollowingDTO.builder()
            .page(1)
            .size(5)
            .isLast(true)
            .total(1L)
            .totalPages(1)
            .followers(List.of(ListFollowingDTO.UserFollowingDTO.builder()
                .followId(1L)
                .userFollowedId(2L)
                .userFollowedEmail("any_email")
                .userFollowedName("any_name")
                .followingSince("any_date")
                .build()))
            .build()
        );

        ListFollowingDTO output = sut.execute(userMockId, pageProvided, sizeProvided, null, "DESC", 10);

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