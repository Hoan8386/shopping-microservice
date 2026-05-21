package com.shoping.productservice.command.command;

import org.axonframework.modelling.command.TargetAggregateIdentifier;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CreateCategoryCommand {

    @TargetAggregateIdentifier
    private String id;

    private String name;

    private String description;

    private Boolean status;
}
