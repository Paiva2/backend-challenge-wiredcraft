package org.com.wired.adapters.infra.persistence.repository.user;

import org.com.wired.application.infra.persistence.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepositoryOrm extends JpaRepository<UserEntity, Long> {

    Optional<UserEntity> findByEmail(String email);
}
