package org.com.wired.domain.ports.inbound.usecase;

import org.com.wired.domain.entity.User;

public interface FindUserUsecasePort {
    User execute(Long userId);
}
