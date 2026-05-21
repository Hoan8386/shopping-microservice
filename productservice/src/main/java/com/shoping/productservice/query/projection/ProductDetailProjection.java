package com.shoping.productservice.query.projection;

import java.util.ArrayList;
import java.util.List;

import org.axonframework.queryhandling.QueryHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.shoping.productservice.command.data.ProductDetail;
import com.shoping.productservice.command.data.ProductDetailRepository;
import com.shoping.productservice.query.model.ProductDetailResponseModel;
import com.shoping.productservice.query.queries.GetAllProductDetailQuery;
import com.shoping.productservice.query.queries.GetProductDetailByIdQuery;

@Component
public class ProductDetailProjection {
    @Autowired
    private ProductDetailRepository productDetailRepository;

    @QueryHandler
    public List<ProductDetailResponseModel> handle(GetAllProductDetailQuery query) {
        List<ProductDetail> details = productDetailRepository.findAll();
        List<ProductDetailResponseModel> responses = new ArrayList<>();
        details.forEach(detail -> responses.add(toResponse(detail)));
        return responses;
    }

    @QueryHandler
    public ProductDetailResponseModel handle(GetProductDetailByIdQuery query) {
        return productDetailRepository.findById(query.getId())
                .map(this::toResponse)
                .orElse(new ProductDetailResponseModel());
    }

    private ProductDetailResponseModel toResponse(ProductDetail detail) {
        ProductDetailResponseModel response = new ProductDetailResponseModel();
        response.setId(detail.getId());
        if (detail.getProduct() != null) {
            response.setProductId(detail.getProduct().getId());
        }
        if (detail.getSize() != null) {
            response.setSizeId(detail.getSize().getId());
        }
        response.setQuantity(detail.getQuantity());
        response.setPrice(detail.getPrice());
        response.setStatus(detail.getStatus());
        return response;
    }
}
