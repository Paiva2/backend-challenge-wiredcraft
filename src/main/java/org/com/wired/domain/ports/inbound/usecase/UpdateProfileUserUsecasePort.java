package org.com.wired.domain.ports.inbound.usecase;

import org.com.wired.application.gateway.output.UpdateProfileUserOutput;
import org.com.wired.domain.entity.User;

public interface UpdateProfileUserUsecasePort {
    UpdateProfileUserOutput execute(Long userId, User input);
}
