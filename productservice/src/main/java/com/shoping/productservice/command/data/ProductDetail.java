package com.shoping.productservice.command.data;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Entity
@Table(name = "product_details")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductDetail {

    @Id
    @Column(name = "product_detail_id")
    private String id;

    // Sản phẩm chính
    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    // Size của sản phẩm
    @ManyToOne
    @JoinColumn(name = "size_id", nullable = false)
    private Size size;

    // Số lượng theo từng size
    @NotNull(message = "Quantity must not be null")
    @Min(value = 0, message = "Quantity must be greater than or equal to 0")
    private Integer quantity;

    // Giá riêng nếu size có giá khác nhau
    private Double price;

    // SKU riêng cho từng biến thể
    // @Column(unique = true)
    // private String sku;

    // Trạng thái
    private Boolean status;
}