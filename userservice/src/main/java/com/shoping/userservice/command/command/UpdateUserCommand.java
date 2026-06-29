package com.shoping.userservice.command.command;

import java.time.LocalDate;

import org.axonframework.modelling.command.TargetAggregateIdentifier;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UpdateUserCommand {

    @TargetAggregateIdentifier
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
