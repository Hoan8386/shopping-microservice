package com.shoping.productservice.query.controller;

import java.util.List;

import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shoping.commonservice.util.anotation.ApiMessage;
import com.shoping.productservice.query.model.ProductDetailResponseModel;
import com.shoping.productservice.query.queries.GetAllProductDetailQuery;
import com.shoping.productservice.query.queries.GetProductDetailByIdQuery;

@RestController
@RequestMapping("/api/v1/product-details")
public class ProductDetailQueryController {
    @Autowired
    private QueryGateway queryGateway;

    @GetMapping
    @ApiMessage("Get product details")
    public ResponseEntity<List<ProductDetailResponseModel>> getAllProductDetails() {
        GetAllProductDetailQuery query = new GetAllProductDetailQuery();
        List<ProductDetailResponseModel> result = queryGateway
                .query(query, ResponseTypes.multipleInstancesOf(ProductDetailResponseModel.class)).join();
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @GetMapping("/{productDetailId}")
    @ApiMessage("Get product detail")
    public ResponseEntity<ProductDetailResponseModel> getProductDetail(@PathVariable String productDetailId) {
        GetProductDetailByIdQuery query = new GetProductDetailByIdQuery(productDetailId);
        ProductDetailResponseModel result = queryGateway
                .query(query, ResponseTypes.instanceOf(ProductDetailResponseModel.class)).join();
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }
}
