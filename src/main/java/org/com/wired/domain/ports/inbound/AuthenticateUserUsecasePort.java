package org.com.wired.domain.ports.inbound;

import org.com.wired.domain.entity.User;

public interface AuthenticateUserUsecasePort {
    Long execute(User user);
}
