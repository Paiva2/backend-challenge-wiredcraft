package org.com.wired.domain.ports.inbound;

import org.com.wired.application.gateway.input.UpdateProfileUserInput;
import org.com.wired.application.gateway.output.UpdateProfileUserOutput;

public interface UpdateProfileUserUsecasePort {
    UpdateProfileUserOutput execute(Long userId, UpdateProfileUserInput input);
}
