package org.com.wired.adapters.infra.persistence.repository.user;

import org.com.wired.application.infra.persistence.entity.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepositoryOrm extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findByEmail(String email);

    @Query(value = "SELECT * FROM tb_users usr " +
        "JOIN tb_address adr ON adr.adr_user_id = usr.usr_id " +
        "WHERE usr_deleted_at IS NULL " +
        "AND (:name IS NULL OR lower(usr_name) LIKE concat('%', lower(:name), '%'))", nativeQuery = true)
    Page<UserEntity> findUsersPage(Pageable pageable, @Param("name") String name);
}
