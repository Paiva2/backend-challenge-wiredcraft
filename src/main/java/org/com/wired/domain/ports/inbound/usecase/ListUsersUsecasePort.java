package org.com.wired.domain.ports.inbound.usecase;

import org.com.wired.domain.usecase.user.listUsers.dto.ListUsersPageDTO;

public interface ListUsersUsecasePort {
    ListUsersPageDTO execute(int page, int size, String name);
}
