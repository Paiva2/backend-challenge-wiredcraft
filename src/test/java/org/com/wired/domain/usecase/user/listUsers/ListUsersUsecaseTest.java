package org.com.wired.domain.usecase.user.listUsers;

import org.com.wired.domain.ports.outbound.infra.persistence.UserRepositoryPort;
import org.com.wired.domain.usecase.user.listUsers.dto.ListUsersPageDTO;
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
class ListUsersUsecaseTest {
    @Mock
    private UserRepositoryPort userRepositoryPort;

    private ListUsersUsecase sut;

    @BeforeEach
    void setUp() {
        this.sut = ListUsersUsecase.builder()
            .userRepositoryPort(userRepositoryPort)
            .build();
    }

    @Test
    void shouldMakePageOneIfLessThanOne() {
        int pageNumberMock = -1;

        when(userRepositoryPort.listUsersPage(anyInt(), anyInt(), any())).thenReturn(ListUsersPageDTO.builder()
            .page(1)
            .size(5)
            .isLast(true)
            .totalItems(1L)
            .totalPages(1)
            .users(List.of())
            .build()
        );

        sut.execute(pageNumberMock, 5, null);

        ArgumentCaptor<Integer> pageNumberCaptor = ArgumentCaptor.forClass(int.class);

        verify(userRepositoryPort, times(1)).listUsersPage(pageNumberCaptor.capture(), anyInt(), any());

        assertEquals(1, pageNumberCaptor.getValue());
    }

    @Test
    void shouldMakeSizeFiveIfLessThanFive() {
        Integer mockPageSize = 1;

        when(userRepositoryPort.listUsersPage(anyInt(), anyInt(), any())).thenReturn(ListUsersPageDTO.builder()
            .page(1)
            .size(5)
            .isLast(true)
            .totalItems(1L)
            .totalPages(1)
            .users(List.of())
            .build()
        );

        sut.execute(1, mockPageSize, null);

        ArgumentCaptor<Integer> sizeCaptor = ArgumentCaptor.forClass(int.class);

        verify(userRepositoryPort, times(1)).listUsersPage(anyInt(), sizeCaptor.capture(), any());

        assertEquals(5, sizeCaptor.getValue());
    }

    @Test
    void shouldMakeSizeFifthyIfMoreThanFifthy() {
        Integer mockPageSize = 100;

        when(userRepositoryPort.listUsersPage(anyInt(), anyInt(), any())).thenReturn(ListUsersPageDTO.builder()
            .page(1)
            .size(5)
            .isLast(true)
            .totalItems(1L)
            .totalPages(1)
            .users(List.of())
            .build()
        );

        sut.execute(1, mockPageSize, null);

        ArgumentCaptor<Integer> sizeCaptor = ArgumentCaptor.forClass(int.class);

        verify(userRepositoryPort, times(1)).listUsersPage(anyInt(), sizeCaptor.capture(), any());

        assertEquals(50, sizeCaptor.getValue());
    }

    @Test
    void shouldReturnSutOutput() {
        int pageProvided = 1;
        int sizeProvided = 5;

        when(userRepositoryPort.listUsersPage(anyInt(), anyInt(), any())).thenReturn(ListUsersPageDTO.builder()
            .page(1)
            .size(5)
            .isLast(true)
            .totalItems(1L)
            .totalPages(1)
            .users(List.of(ListUsersPageDTO.UserOutput.builder()
                .id(2L)
                .name("any_name")
                .email("any_email")
                .birthDate("any_date")
                .profilePictureUrl("any_profile_picture")
                .build()
            )).build()
        );

        ListUsersPageDTO output = sut.execute(pageProvided, sizeProvided, null);

        assertEquals(pageProvided, output.getPage());
        assertEquals(sizeProvided, output.getSize());
        assertEquals(1L, output.getTotalItems());
        assertEquals(1, output.getTotalPages());
        assertTrue(output.isLast());
        assertFalse(output.getUsers().isEmpty());
    }
}