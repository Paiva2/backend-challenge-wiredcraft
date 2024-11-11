package org.com.wired.domain.usecase.user.listUsers;

import lombok.AllArgsConstructor;
import lombok.Builder;
import org.com.wired.domain.ports.inbound.usecase.ListUsersUsecasePort;
import org.com.wired.domain.ports.outbound.infra.persistence.UserRepositoryPort;
import org.com.wired.domain.usecase.user.listUsers.dto.ListUsersPageDTO;

@AllArgsConstructor
@Builder
public class ListUsersUsecase implements ListUsersUsecasePort {
    private final UserRepositoryPort userRepositoryPort;

    public ListUsersPageDTO execute(int page, int size, String name) {
        if (page < 1) {
            page = 1;
        }

        if (size < 5) {
            size = 5;
        } else if (size > 50) {
            size = 50;
        }

        ListUsersPageDTO usersPage = findUsersPage(page, size, name);
        return usersPage;
    }

    private ListUsersPageDTO findUsersPage(int page, int size, String name) {
        return userRepositoryPort.listUsersPage(page, size, name);
    }
}
