package org.com.wired.application.factory.user;

import lombok.AllArgsConstructor;
import org.com.wired.adapters.strategy.emailValidator.EmailValidatorStrategyConcrete;
import org.com.wired.adapters.strategy.emailValidator.emailRegexValidator.EmailRegexValidator;
import org.com.wired.domain.ports.outbound.infra.persistence.AddressRepositoryPort;
import org.com.wired.domain.ports.outbound.infra.persistence.UserRepositoryPort;
import org.com.wired.domain.ports.outbound.utils.PasswordUtilsPort;
import org.com.wired.domain.usecase.user.findUserUsecase.FindUserUsecase;
import org.com.wired.domain.usecase.user.updateProfileUser.UpdateProfileUserUsecase;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

@AllArgsConstructor
@Configuration
public class UpdateProfileUserFactory {
    private final UserRepositoryPort userRepositoryPort;
    private final AddressRepositoryPort addressRepositoryPort;
    private final PasswordUtilsPort passwordUtilsPort;
    private final FindUserUsecase findUserUsecase;


    @Bean("UpdateProfileUserUsecase")
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public UpdateProfileUserUsecase create() {
        return UpdateProfileUserUsecase.builder()
            .userRepositoryPort(userRepositoryPort)
            .addressRepositoryPort(addressRepositoryPort)
            .emailValidatorStrategyPort(new EmailValidatorStrategyConcrete(new EmailRegexValidator()))
            .passwordUtilsPort(passwordUtilsPort)
            .findUserUsecase(findUserUsecase)
            .build();
    }
}
