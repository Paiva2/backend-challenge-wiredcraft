package org.com.wired.domain.ports.inbound;

import org.com.wired.application.gateway.output.RegisterUserOutput;
import org.com.wired.domain.entity.User;

public interface RegisterUserUsecasePort {
    RegisterUserOutput execute(User input);
}
