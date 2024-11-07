package org.com.wired.application.infra.persistence.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.util.Date;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@Entity
@Table(name = "tb_followers")
public class FollowerEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "fol_id")
    private Long id;

    @Column(name = "fol_created_at", updatable = false, nullable = false)
    @CreationTimestamp
    private Date createdAt;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "fol_user_id", nullable = false)
    private UserEntity user;

    @OneToMany(mappedBy = "follower", fetch = FetchType.LAZY)
    private List<UserFollowerEntity> userFollowers;
}
