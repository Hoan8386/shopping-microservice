package com.shoping.userservice.command.command;

import org.axonframework.modelling.command.TargetAggregateIdentifier;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CreatePermissionCommand {
    @TargetAggregateIdentifier
    private String id;

    private String code;

    private String name;

    private String module;

    private String apiPath;

    private String method;
}
