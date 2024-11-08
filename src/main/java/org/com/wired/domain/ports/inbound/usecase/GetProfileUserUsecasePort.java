package org.com.wired.domain.ports.inbound.usecase;

import org.com.wired.application.gateway.output.GetProfileUserOutput;

public interface GetProfileUserUsecasePort {
    GetProfileUserOutput execute(Long userId);
}
