package org.com.wired.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class Address {
    private Long id;
    private String street;
    private String number;
    private String neighbourhood;
    private String state;
    private String cityName;
    private String country;
    private String zipCode;
    private String complement;
    private Double latitude;
    private Double longitude;
    private Date createdAt;
    private Date updatedAt;

    private User user;
}
