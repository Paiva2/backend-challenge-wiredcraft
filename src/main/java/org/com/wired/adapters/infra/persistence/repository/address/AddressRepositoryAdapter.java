package org.com.wired.adapters.infra.persistence.repository.address;

import lombok.AllArgsConstructor;
import org.com.wired.adapters.infra.mapper.AddressMapperAdapter;
import org.com.wired.application.infra.persistence.entity.AddressEntity;
import org.com.wired.domain.entity.Address;
import org.com.wired.domain.ports.outbound.infra.persistence.AddressRepositoryPort;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@Component
public class AddressRepositoryAdapter implements AddressRepositoryPort {
    private final AddressRepositoryOrm repository;

    @Override
    public Address persist(Address address) {
        AddressEntity addressEntity = repository.save(AddressMapperAdapter.toPersistence(address));
        return AddressMapperAdapter.toDomain(addressEntity);
    }
}
