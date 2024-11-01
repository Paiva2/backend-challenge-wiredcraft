package org.com.wired.adapters.strategy.emailValidator;

import lombok.AllArgsConstructor;
import org.com.wired.domain.ports.outbound.strategy.EmailValidatorStrategyPort;

@AllArgsConstructor
public class EmailValidatorStrategyConcrete implements EmailValidatorStrategyPort {
    private final EmailValidatorStrategy emailValidatorStrategy;

    public boolean execute(String email) {
        return emailValidatorStrategy.validate(email);
    }
}
