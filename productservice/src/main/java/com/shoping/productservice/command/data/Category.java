package com.shoping.productservice.command.data;

import jakarta.persistence.*;

import lombok.*;

@Entity
@Table(name = "categories")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Category {

    @Id
    @Column(name = "category_id")
    private String id;

    private String name;

    private String description;

    private Boolean status;
}