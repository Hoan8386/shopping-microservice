package com.shoping.cartservice.command.model;

import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UpdateCartItemRequestModel {

    @Min(value = 1, message = "Quantity must be at least 1")
    private int quantity;
}
