package org.com.wired.adapters.infra.persistence.repository.address;

import lombok.AllArgsConstructor;
import org.com.wired.adapters.infra.mapper.AddressMapperAdapter;
import org.com.wired.application.infra.persistence.entity.AddressEntity;
import org.com.wired.domain.entity.Address;
import org.com.wired.domain.ports.outbound.infra.persistence.AddressRepositoryPort;
import org.springframework.stereotype.Component;

import java.util.Optional;

@AllArgsConstructor
@Component
public class AddressRepositoryAdapter implements AddressRepositoryPort {
    private final AddressRepositoryOrm repository;

    @Override
    public Address persist(Address address) {
        AddressEntity addressEntity = repository.save(AddressMapperAdapter.toPersistence(address));
        return AddressMapperAdapter.toDomain(addressEntity);
    }

    @Override
    public Optional<Address> findByUserId(Long userId) {
        Optional<AddressEntity> addressEntity = repository.findByUserId(userId);
        if (addressEntity.isEmpty()) return Optional.empty();
        return Optional.of(AddressMapperAdapter.toDomain(addressEntity.get()));
    }
}
