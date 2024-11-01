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
public class User {
    private Long id;
    private String email;
    private String password;
    private String name;
    private String profilePictureUrl;
    private Date birthDate;
    private String description;
    private Date createdAt;
    private Date updatedAt;
    private Date disabledAt;

    private Address address;
}
