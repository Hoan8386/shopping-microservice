package com.shoping.employeeservice.command.command;

import org.axonframework.modelling.command.TargetAggregateIdentifier;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UpdateEmployeeCommand {

    @TargetAggregateIdentifier
    private String id;

    private String firstName;

    private String lastName;

    private String kin;

    private Boolean isDisciplined;

    private String email;

    private String address;

    private String avatar;
}
