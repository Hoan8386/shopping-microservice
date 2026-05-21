package com.shoping.productservice.query.controller;

import java.util.List;

import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shoping.productservice.query.model.CategoryResponseModel;
import com.shoping.productservice.query.model.ProductResponseModel;
import com.shoping.productservice.query.queries.FilterProductQuery;
import com.shoping.productservice.query.queries.GetAllCategoryQuery;
import com.shoping.productservice.query.queries.GetAllProductQuery;
import com.shoping.productservice.query.queries.GetProductDetailQuery;
import com.shoping.productservice.query.queries.GetCategoryDetailQuery;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import com.shoping.commonservice.util.anotation.ApiMessage;

@RestController
@RequestMapping("/api/v1/category")
public class CategoryQueryController {

    @Autowired
    private QueryGateway queryGateway;

    @GetMapping
    @ApiMessage("Get categories")
    public ResponseEntity<List<CategoryResponseModel>> GetAllProducts() {
        GetAllCategoryQuery getAllProductQuery = new GetAllCategoryQuery();
        List<CategoryResponseModel> result = queryGateway
                .query(getAllProductQuery, ResponseTypes.multipleInstancesOf(CategoryResponseModel.class)).join();
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @GetMapping("/{categoryId}")
    @ApiMessage("Get category detail")
    public ResponseEntity<CategoryResponseModel> GetCategoryDetail(@PathVariable String categoryId) {
        GetCategoryDetailQuery query = new GetCategoryDetailQuery(categoryId);
        CategoryResponseModel result = queryGateway
                .query(query, ResponseTypes.instanceOf(CategoryResponseModel.class)).join();
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }
}
