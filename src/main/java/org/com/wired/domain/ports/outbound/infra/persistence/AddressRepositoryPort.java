package org.com.wired.domain.ports.outbound.infra.persistence;

import org.com.wired.domain.entity.Address;

import java.util.Optional;

public interface AddressRepositoryPort {
    Address persist(Address address);

    Optional<Address> findByUserId(Long userId);
}
