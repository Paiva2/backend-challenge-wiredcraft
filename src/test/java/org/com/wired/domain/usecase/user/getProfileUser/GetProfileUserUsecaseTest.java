package org.com.wired.domain.usecase.user.getProfileUser;

import org.com.wired.adapters.utils.DateUtilsAdapter;
import org.com.wired.application.gateway.output.GetProfileUserOutput;
import org.com.wired.domain.entity.Address;
import org.com.wired.domain.entity.User;
import org.com.wired.domain.ports.outbound.infra.persistence.AddressRepositoryPort;
import org.com.wired.domain.usecase.common.exception.AddressNotFoundException;
import org.com.wired.domain.usecase.user.findUserUsecase.FindUserUsecase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Date;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GetProfileUserUsecaseTest {
    @Mock
    private FindUserUsecase findUserUsecase;

    @Mock
    private AddressRepositoryPort addressRepositoryPort;

    private GetProfileUserUsecase sut;

    @BeforeEach
    void setUp() {
        this.sut = GetProfileUserUsecase.builder()
            .findUserUsecase(findUserUsecase)
            .addressRepositoryPort(addressRepositoryPort)
            .build();
    }

    @Test
    void shouldThrowExceptionIfUserAddressNotFound() {
        Long mockSutInput = 1L;

        User userMock = mockUser(
            1L,
            "any_name",
            "any_email",
            "any_profile_pic",
            new Date(),
            "any_description"
        );

        when(findUserUsecase.execute(mockSutInput)).thenReturn(userMock);
        when(addressRepositoryPort.findByUserId(mockSutInput)).thenReturn(Optional.empty());

        Exception error = assertThrows(AddressNotFoundException.class, () -> {
            sut.execute(mockSutInput);
        });

        assertEquals("Address not found!", error.getMessage());
        assertEquals("AddressNotFoundException", error.getClass().getSimpleName());
    }

    @Test
    void shouldSearchForUserAddressByCorrectUserId() {
        Long mockSutInput = 1L;

        User userMock = mockUser(
            1L,
            "any_name",
            "any_email",
            "any_profile_pic",
            new Date(),
            "any_description"
        );

        Address addressMock = mockAddress(
            1L,
            "any_street",
            "any_number",
            "any_neighbourhood",
            "any_state",
            "any_city",
            "any_country",
            "any_zipCode",
            "any_complement"
        );

        when(findUserUsecase.execute(mockSutInput)).thenReturn(userMock);
        when(addressRepositoryPort.findByUserId(anyLong())).thenReturn(Optional.of(addressMock));

        ArgumentCaptor<Long> userIdCaptor = ArgumentCaptor.forClass(Long.class);

        sut.execute(mockSutInput);

        verify(addressRepositoryPort, times(1)).findByUserId(userIdCaptor.capture());
        assertEquals(userMock.getId(), userIdCaptor.getValue());
    }

    @Test
    void shouldReturnSutOutput() {
        Long mockSutInput = 1L;

        User userMock = mockUser(
            1L,
            "any_name",
            "any_email",
            "any_profile_pic",
            new Date(),
            "any_description"
        );

        Address addressMock = mockAddress(
            1L,
            "any_street",
            "any_number",
            "any_neighbourhood",
            "any_state",
            "any_city",
            "any_country",
            "any_zipCode",
            "any_complement"
        );

        when(findUserUsecase.execute(mockSutInput)).thenReturn(userMock);
        when(addressRepositoryPort.findByUserId(anyLong())).thenReturn(Optional.of(addressMock));

        GetProfileUserOutput output = sut.execute(mockSutInput);

        assertEquals(userMock.getId(), output.getId());
        assertEquals(userMock.getName(), output.getName());
        assertEquals(userMock.getEmail(), output.getEmail());
        assertEquals(userMock.getDescription(), output.getDescription());
        assertEquals(userMock.getProfilePictureUrl(), output.getProfilePictureUrl());
        assertEquals(formatDateOutput(userMock.getBirthDate()), output.getBirthDate());

        assertEquals(addressMock.getStreet(), output.getAddress().getStreet());
        assertEquals(addressMock.getNumber(), output.getAddress().getNumber());
        assertEquals(addressMock.getState(), output.getAddress().getState());
        assertEquals(addressMock.getCountry(), output.getAddress().getCountry());
        assertEquals(addressMock.getNeighbourhood(), output.getAddress().getNeighbourhood());
        assertEquals(addressMock.getCityName(), output.getAddress().getCityName());
        assertEquals(addressMock.getZipCode(), output.getAddress().getZipCode());
        assertEquals(addressMock.getComplement(), output.getAddress().getComplement());
    }

    private User mockUser(Long id, String name, String email, String profilePic, Date birthDate, String description) {
        return User.builder()
            .id(id)
            .name(name)
            .email(email)
            .profilePictureUrl(profilePic)
            .birthDate(birthDate)
            .description(description)
            .password("hashed_password")
            .build();
    }

    private Address mockAddress(Long id, String street, String number, String neighbourhood, String state, String city, String country, String zipCode, String complement) {
        return Address.builder()
            .id(id)
            .street(street)
            .number(number)
            .neighbourhood(neighbourhood)
            .state(state)
            .cityName(city)
            .country(country)
            .zipCode(zipCode)
            .complement(complement)
            .updatedAt(new Date())
            .build();
    }

    private String formatDateOutput(Date date) {
        DateUtilsAdapter dateUtilsAdapter = new DateUtilsAdapter();
        return dateUtilsAdapter.dateToOutputString(date);
    }
}