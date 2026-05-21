package com.shoping.productservice.command.data;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Entity
@Table(name = "sizes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Size {

    @Id
    @Column(name = "size_id")
    private String id;

    @Column(nullable = false, unique = true)
    @NotBlank(message = "Size name must not be blank")
    @jakarta.validation.constraints.Size(max = 20, message = "Size name must not exceed 20 characters")
    private String name;

    @jakarta.validation.constraints.Size(max = 255, message = "Description must not exceed 255 characters")
    private String description;

    private Boolean status;
}