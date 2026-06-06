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

    private String name;

    private String description;

    private Boolean status;
}