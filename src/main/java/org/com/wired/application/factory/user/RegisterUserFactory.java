package org.com.wired.application.factory.user;

import lombok.AllArgsConstructor;
import org.com.wired.adapters.strategy.emailValidator.EmailValidatorStrategyConcrete;
import org.com.wired.adapters.strategy.emailValidator.emailRegexValidator.EmailRegexValidator;
import org.com.wired.domain.ports.outbound.infra.persistence.AddressRepositoryPort;
import org.com.wired.domain.ports.outbound.infra.persistence.UserRepositoryPort;
import org.com.wired.domain.ports.outbound.utils.PasswordUtilsPort;
import org.com.wired.domain.usecase.user.registerUser.RegisterUserUsecase;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

@AllArgsConstructor
@Configuration
public class RegisterUserFactory {
    private final UserRepositoryPort userRepositoryPort;
    private final AddressRepositoryPort addressRepositoryPort;
    private final PasswordUtilsPort passwordUtilsPort;

    @Bean("RegisterUserUsecase")
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public RegisterUserUsecase create() {
        return RegisterUserUsecase.builder()
            .userRepositoryPort(userRepositoryPort)
            .addressRepositoryPort(addressRepositoryPort)
            .passwordUtilsPort(passwordUtilsPort)
            .emailValidatorStrategyPort(new EmailValidatorStrategyConcrete(new EmailRegexValidator()))
            .build();
    }
}
