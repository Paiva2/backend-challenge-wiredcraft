package org.com.wired.domain.usecase.user.registerUser;

import org.com.wired.adapters.utils.DateUtilsAdapter;
import org.com.wired.application.gateway.output.RegisterUserOutput;
import org.com.wired.domain.entity.Address;
import org.com.wired.domain.entity.User;
import org.com.wired.domain.ports.outbound.infra.persistence.AddressRepositoryPort;
import org.com.wired.domain.ports.outbound.infra.persistence.UserRepositoryPort;
import org.com.wired.domain.ports.outbound.strategy.EmailValidatorStrategyPort;
import org.com.wired.domain.ports.outbound.utils.PasswordUtilsPort;
import org.com.wired.domain.usecase.common.exception.InvalidPropertyException;
import org.com.wired.domain.usecase.user.registerUser.exception.EmailAlreadyInUseException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.text.MessageFormat;
import java.util.Date;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RegisterUserUsecaseTest {
    @Mock
    private UserRepositoryPort userRepositoryPort;

    @Mock
    private AddressRepositoryPort addressRepositoryPort;

    @Mock
    private EmailValidatorStrategyPort emailValidatorStrategyPort;

    @Mock
    private PasswordUtilsPort passwordUtilsPort;

    private RegisterUserUsecase sut;

    @BeforeEach
    void setUp() {
        this.sut = RegisterUserUsecase.builder()
            .userRepositoryPort(userRepositoryPort)
            .addressRepositoryPort(addressRepositoryPort)
            .emailValidatorStrategyPort(emailValidatorStrategyPort)
            .passwordUtilsPort(passwordUtilsPort)
            .build();
    }

    @Test
    void shouldThrowExceptionIfEmailFormatIsInvalid() {
        User sutInput = mockInput();
        sutInput.setEmail("any_invalid_email");

        when(emailValidatorStrategyPort.execute("any_invalid_email")).thenReturn(false);

        Exception error = assertThrows(InvalidPropertyException.class, () -> {
            sut.execute(sutInput);
        });

        assertEquals("Invalid property. Reason: Invalid e-mail.", error.getMessage());
        assertEquals("InvalidPropertyException", error.getClass().getSimpleName());
    }

    @Test
    void shouldThrowExceptionIfEmailIsAlreadyBeingUsed() {
        User sutInput = mockInput();

        when(emailValidatorStrategyPort.execute(anyString())).thenReturn(true);
        when(userRepositoryPort.findByEmail(anyString())).thenReturn(Optional.of(new User()));

        Exception error = assertThrows(EmailAlreadyInUseException.class, () -> {
            sut.execute(sutInput);
        });

        assertEquals(MessageFormat.format("Email {0} is already in use.", sutInput.getEmail()), error.getMessage());
        assertEquals("EmailAlreadyInUseException", error.getClass().getSimpleName());
    }

    @Test
    void shouldThrowExceptionIfPasswordHasLessThanNecessaryCharacters() {
        User sutInput = mockInput();
        sutInput.setPassword("invalid_password");

        when(emailValidatorStrategyPort.execute(anyString())).thenReturn(true);
        when(userRepositoryPort.findByEmail(anyString())).thenReturn(Optional.empty());
        when(passwordUtilsPort.hasNecessaryLength(anyString())).thenReturn(false);

        Exception error = assertThrows(InvalidPropertyException.class, () -> {
            sut.execute(sutInput);
        });

        assertEquals("Invalid property. Reason: Password must have at least 6 characters.", error.getMessage());
        assertEquals("InvalidPropertyException", error.getClass().getSimpleName());
    }

    @Test
    void shouldHashUserPasswordCorrectly() {
        User sutInput = mockInput();

        when(emailValidatorStrategyPort.execute(anyString())).thenReturn(true);
        when(userRepositoryPort.findByEmail(anyString())).thenReturn(Optional.empty());
        when(passwordUtilsPort.hasNecessaryLength(anyString())).thenReturn(true);
        when(passwordUtilsPort.encode(anyString())).thenReturn("hashed_password");

        User userMock = mockUser(
            1L,
            sutInput.getName(),
            sutInput.getEmail(),
            sutInput.getProfilePictureUrl(),
            sutInput.getBirthDate(),
            sutInput.getDescription()
        );

        when(userRepositoryPort.persist(any())).thenReturn(userMock);

        ArgumentCaptor<String> rawPasswordProvidedCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<User> userProvidedCaptor = ArgumentCaptor.forClass(User.class);

        sut.execute(sutInput);

        verify(passwordUtilsPort, times(1)).encode(rawPasswordProvidedCaptor.capture());
        verify(userRepositoryPort, times(1)).persist(userProvidedCaptor.capture());

        String rawPasswordProvided = rawPasswordProvidedCaptor.getValue();
        String passwordHashedSaved = userProvidedCaptor.getValue().getPassword();

        assertEquals("any_password", rawPasswordProvided);
        assertEquals("hashed_password", passwordHashedSaved);
    }

    @Test
    void shouldPersistUserWithCorrectlyInputData() {
        User sutInput = mockInput();

        when(emailValidatorStrategyPort.execute(anyString())).thenReturn(true);
        when(userRepositoryPort.findByEmail(anyString())).thenReturn(Optional.empty());
        when(passwordUtilsPort.hasNecessaryLength(anyString())).thenReturn(true);
        when(passwordUtilsPort.encode(anyString())).thenReturn("hashed_password");

        User userMock = mockUser(
            1L,
            sutInput.getName(),
            sutInput.getEmail(),
            sutInput.getProfilePictureUrl(),
            sutInput.getBirthDate(),
            sutInput.getDescription()
        );

        when(userRepositoryPort.persist(any())).thenReturn(userMock);

        ArgumentCaptor<User> userProvidedCaptor = ArgumentCaptor.forClass(User.class);

        sut.execute(sutInput);

        verify(userRepositoryPort, times(1)).persist(userProvidedCaptor.capture());

        assertEquals(sutInput.getEmail(), userProvidedCaptor.getValue().getEmail());
        assertEquals(sutInput.getName(), userProvidedCaptor.getValue().getName());
        assertEquals(sutInput.getProfilePictureUrl(), userProvidedCaptor.getValue().getProfilePictureUrl());
        assertEquals(sutInput.getDescription(), userProvidedCaptor.getValue().getDescription());
        assertEquals(sutInput.getBirthDate(), userProvidedCaptor.getValue().getBirthDate());
    }

    @Test
    void shouldPersistAddressWithCorrectlyInputData() {
        User sutInput = mockInput();

        when(emailValidatorStrategyPort.execute(anyString())).thenReturn(true);
        when(userRepositoryPort.findByEmail(anyString())).thenReturn(Optional.empty());
        when(passwordUtilsPort.hasNecessaryLength(anyString())).thenReturn(true);
        when(passwordUtilsPort.encode(anyString())).thenReturn("hashed_password");

        User userMock = mockUser(
            1L,
            sutInput.getName(),
            sutInput.getEmail(),
            sutInput.getProfilePictureUrl(),
            sutInput.getBirthDate(),
            sutInput.getDescription()
        );

        when(userRepositoryPort.persist(any())).thenReturn(userMock);

        ArgumentCaptor<Address> addressProvidedCaptor = ArgumentCaptor.forClass(Address.class);

        sut.execute(sutInput);

        verify(addressRepositoryPort, times(1)).persist(addressProvidedCaptor.capture());

        assertEquals(sutInput.getAddress().getStreet(), addressProvidedCaptor.getValue().getStreet());
        assertEquals(sutInput.getAddress().getCityName(), addressProvidedCaptor.getValue().getCityName());
        assertEquals(sutInput.getAddress().getNeighbourhood(), addressProvidedCaptor.getValue().getNeighbourhood());
        assertEquals(sutInput.getAddress().getCountry(), addressProvidedCaptor.getValue().getCountry());
        assertEquals(sutInput.getAddress().getComplement(), addressProvidedCaptor.getValue().getComplement());
        assertEquals(sutInput.getAddress().getNumber(), addressProvidedCaptor.getValue().getNumber());
        assertEquals(sutInput.getAddress().getZipCode(), addressProvidedCaptor.getValue().getZipCode());
        assertEquals(sutInput.getAddress().getState(), addressProvidedCaptor.getValue().getState());
        assertEquals(sutInput.getAddress().getUser().getId(), userMock.getId());
    }

    @Test
    void shouldReturnSutOutput() {
        User sutInput = mockInput();

        when(emailValidatorStrategyPort.execute(anyString())).thenReturn(true);
        when(userRepositoryPort.findByEmail(anyString())).thenReturn(Optional.empty());
        when(passwordUtilsPort.hasNecessaryLength(anyString())).thenReturn(true);
        when(passwordUtilsPort.encode(anyString())).thenReturn("hashed_password");

        User userMock = mockUser(
            1L,
            sutInput.getName(),
            sutInput.getEmail(),
            sutInput.getProfilePictureUrl(),
            sutInput.getBirthDate(),
            sutInput.getDescription()
        );

        when(userRepositoryPort.persist(any())).thenReturn(userMock);


        RegisterUserOutput output = sut.execute(sutInput);

        assertEquals(sutInput.getEmail(), output.getEmail());
        assertEquals(sutInput.getName(), output.getName());
        assertEquals(sutInput.getProfilePictureUrl(), output.getProfilePictureUrl());
        assertEquals(sutInput.getDescription(), output.getDescription());
        assertEquals(formatDateOutput(sutInput.getBirthDate()), output.getBirthDate());
    }

    private User mockInput() {
        return User.builder()
            .email("any_email")
            .password("any_password")
            .name("any_name")
            .birthDate(new Date())
            .description("any_description")
            .profilePictureUrl("any_profile_picture")
            .address(Address.builder()
                .cityName("any_city_name")
                .country("any_country")
                .complement("any_complement")
                .neighbourhood("any_neighbourhood")
                .state("any_state")
                .number("any_number")
                .zipCode("any_zip_code")
                .build()
            ).build();
    }

    private User mockUser(Long id, String name, String email, String profilePic, Date birthDate, String description) {
        return User.builder()
            .id(id)
            .name(name)
            .email(email)
            .profilePictureUrl(profilePic)
            .birthDate(birthDate)
            .description(description)
            .build();
    }

    private String formatDateOutput(Date date) {
        DateUtilsAdapter dateUtilsAdapter = new DateUtilsAdapter();
        return dateUtilsAdapter.dateToOutputString(date);
    }
}