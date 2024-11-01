package org.com.wired.application.infra.persistence.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
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

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "adr_user_id")
    private UserEntity user;
}
