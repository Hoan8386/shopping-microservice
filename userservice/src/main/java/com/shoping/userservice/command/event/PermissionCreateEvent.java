package com.shoping.userservice.command.event;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PermissionCreateEvent {
    private String id;

    private String code;

    private String name;

    private String module;

    private String apiPath;

    private String method;
}
