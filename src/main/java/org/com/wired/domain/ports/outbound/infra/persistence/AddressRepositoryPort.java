package org.com.wired.domain.ports.outbound.infra.persistence;

import org.com.wired.domain.entity.Address;

public interface AddressRepositoryPort {
    Address persist(Address address);
}
