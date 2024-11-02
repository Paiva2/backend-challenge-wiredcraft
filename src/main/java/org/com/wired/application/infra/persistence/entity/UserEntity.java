package org.com.wired.application.infra.persistence.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@Entity
@Table(name = "tb_users")
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "usr_id")
    private Long id;

    @Column(name = "usr_email", unique = true, nullable = false, length = 255)
    private String email;

    @Column(name = "usr_password", nullable = false)
    private String password;

    @Column(name = "usr_name", nullable = false, length = 100)
    private String name;

    @Column(name = "usr_profile_picture_url", nullable = true)
    private String profilePictureUrl;

    @Column(name = "usr_birth_date", nullable = false, length = 15)
    private Date birthDate;

    @Column(name = "usr_description", nullable = true, length = 200)
    private String description;

    @CreationTimestamp
    @Column(name = "usr_created_at", nullable = false, updatable = false)
    private Date createdAt;

    @UpdateTimestamp
    @Column(name = "usr_updated_at", nullable = false, updatable = true)
    private Date updatedAt;

    @Column(name = "usr_deleted_at", nullable = true)
    private Date disabledAt;

    @OneToOne(fetch = FetchType.LAZY)
    private AddressEntity address;
}
