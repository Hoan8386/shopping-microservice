package com.shoping.employeeservice.command.data;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "employees")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Employee {

    @Id
    private String id;

    @Column(name = "user_id", unique = true, nullable = false)
    private String userId;

    private String firstName;

    private String lastName;

    private String kin;

    private Boolean isDisciplined;

    private String email;

    private String address;

    private String avatar;
}
