package com.shoping.productservice.command.model;

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
public class SizeRequestModel {

    private String id; // Trường hợp dùng cho API Update nếu cần

    @NotBlank(message = "Size name must not be blank")
    @Size(max = 20, message = "Size name must not exceed 20 characters")
    private String name;

    @Size(max = 255, message = "Description must not exceed 255 characters")
    private String description;

    private Boolean status;
}