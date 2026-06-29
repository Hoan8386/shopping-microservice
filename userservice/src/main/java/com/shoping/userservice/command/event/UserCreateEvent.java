package com.shoping.userservice.command.event;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserCreateEvent {

    private String id;

    private String keycloakId;

    private String fullName;

    private String phone;

    private String address;

    private String avatar;

    private String gender;

    private LocalDate birthday;

    private String status;
}
