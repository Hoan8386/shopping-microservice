package com.shoping.productservice.query.projection;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.axonframework.queryhandling.QueryHandler;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Component;

import com.shoping.productservice.command.data.Product;
import com.shoping.productservice.command.data.ProductRepository;
import com.shoping.productservice.command.model.ProductRequestModel;
import com.shoping.productservice.query.model.ProductResponseModel;
import com.shoping.productservice.query.queries.FilterProductQuery;
import com.shoping.productservice.query.queries.GetAllProductQuery;
import com.shoping.productservice.query.queries.GetProductDetailQuery;
import com.shoping.productservice.query.specification.ProductSpecification;

@Component
public class BookProjection {
    @Autowired
    private ProductRepository productRepository;

    @QueryHandler
    public List<ProductResponseModel> handle(GetAllProductQuery query) {
        List<Product> list = productRepository.findAll();
        List<ProductResponseModel> listProductResponse = new ArrayList<>();
        list.forEach(product -> {
            ProductResponseModel model = new ProductResponseModel();
            BeanUtils.copyProperties(product, model);
            listProductResponse.add(model);
        });
        return listProductResponse;
    }

    @QueryHandler
    public ProductResponseModel handle(GetProductDetailQuery query) {
        ProductResponseModel response = new ProductResponseModel();

        productRepository.findById(query.getId()).ifPresent(
                product -> {
                    BeanUtils.copyProperties(product, response);
                });
        return response;

    }

    @QueryHandler
    public List<ProductResponseModel> handle(FilterProductQuery query) {
        List<ProductResponseModel> listProductResponse = new ArrayList<>();
        List<Product> products = productRepository.findAll(ProductSpecification.filterProducts(
                query.getName(),
                query.getCategory(),
                query.getMinPrice(),
                query.getMaxPrice()));
        products.forEach(product -> {
            ProductResponseModel item = new ProductResponseModel();
            BeanUtils.copyProperties(product, item);
            listProductResponse.add(item);
        });
        return listProductResponse;

    }
}
