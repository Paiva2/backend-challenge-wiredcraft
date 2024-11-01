package org.com.wired.domain.ports.outbound.strategy;

public interface EmailValidatorStrategyPort {
    boolean execute(String email);
}
