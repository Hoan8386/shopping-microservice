package com.shoping.productservice.command.event;

import java.util.Optional;

import org.axonframework.eventhandling.EventHandler;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.shoping.productservice.command.data.Category;
import com.shoping.productservice.command.data.CategoryRepository;
import com.shoping.productservice.command.data.Product;

@Component
public class CategoryEventHandler {
    @Autowired
    private CategoryRepository categoryRepository;

    @EventHandler
    public void on(CategoryCreateEvent event) {
        Category category = new Category();
        BeanUtils.copyProperties(event, category);
        categoryRepository.save(category);
    }

    @EventHandler
    public void on(CategoryUpdateEvent event) {
        Optional<Category> oldCategory = categoryRepository.findById(event.getId());
        if (oldCategory.isPresent()) {
            Category Category = oldCategory.get();
            Category.setName(event.getName());
            Category.setDescription(event.getDescription());
            Category.setStatus(event.getStatus());
            categoryRepository.save(Category);
        }
    }

    @EventHandler
    public void on(CategoryDeleteEvent event) {
        Optional<Category> oldCategory = categoryRepository.findById(event.getId());
        if (oldCategory.isPresent()) {
            categoryRepository.deleteById(event.getId());
        }
    }
}
