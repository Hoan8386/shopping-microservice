package com.shoping.productservice.command.event;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SizeUpdateEvent {
    private String id;

    private String name;

    private String description;

    private Boolean status;
}
