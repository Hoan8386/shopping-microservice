package com.shoping.productservice.query.projection;

import java.util.ArrayList;
import java.util.List;

import org.axonframework.queryhandling.QueryHandler;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.shoping.productservice.command.data.Category;
import com.shoping.productservice.command.data.CategoryRepository;
import com.shoping.productservice.command.data.ProductRepository;
import com.shoping.productservice.query.model.CategoryResponseModel;
import com.shoping.productservice.query.model.ProductResponseModel;
import com.shoping.productservice.query.queries.GetAllCategoryQuery;
import com.shoping.productservice.query.queries.GetCategoryDetailQuery;

@Component
public class CategoryProjection {
    @Autowired
    private CategoryRepository categoryRepository;

    @QueryHandler
    public List<CategoryResponseModel> handle(GetAllCategoryQuery query) {
        List<Category> list = categoryRepository.findAll();
        List<CategoryResponseModel> listCategoryResponseModels = new ArrayList<>();
        list.forEach(product -> {
            CategoryResponseModel model = new CategoryResponseModel();
            BeanUtils.copyProperties(product, model);
            listCategoryResponseModels.add(model);
        });
        return listCategoryResponseModels;
    }

    @QueryHandler
    public CategoryResponseModel handle(GetCategoryDetailQuery query) {
        CategoryResponseModel response = new CategoryResponseModel();
        categoryRepository.findById(query.getId()).ifPresent(category -> {
            BeanUtils.copyProperties(category, response);
        });
        return response;
    }

}
