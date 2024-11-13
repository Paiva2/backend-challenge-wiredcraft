package org.com.wired.application.infra.persistence.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@Entity
@Table(name = "tb_address")
public class AddressEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "adr_id")
    private Long id;

    @Column(name = "adr_street", nullable = false, length = 50)
    private String street;

    @Column(name = "adr_number", nullable = false, length = 15)
    private String number;

    @Column(name = "adr_neighbourhood", nullable = false, length = 50)
    private String neighbourhood;

    @Column(name = "adr_state", nullable = false, length = 20)
    private String state;

    @Column(name = "adr_city_name", nullable = false, length = 20)
    private String cityName;

    @Column(name = "adr_country", nullable = false, length = 50)
    private String country;

    @Column(name = "adr_zipcode", nullable = false, length = 50)
    private String zipCode;

    @Column(name = "adr_complement", nullable = true, length = 100)
    private String complement;

    @Column(name = "adr_latitude", nullable = true, length = 100)
    private Double latitude;

    @Column(name = "adr_longitude", nullable = true, length = 100)
    private Double longitude;

    @Column(name = "adr_updated_at", nullable = true)
    @UpdateTimestamp
    private Date updatedAt;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "adr_user_id")
    private UserEntity user;
}
