package org.com.wired.adapters.infra.persistence.repository.address;

import org.com.wired.application.infra.persistence.entity.AddressEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AddressRepositoryOrm extends JpaRepository<AddressEntity, Long> {
}
