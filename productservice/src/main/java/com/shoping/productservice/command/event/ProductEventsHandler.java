package com.shoping.productservice.command.event;

import java.util.Optional;

import org.axonframework.eventhandling.EventHandler;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import com.shoping.productservice.command.data.Product;
import com.shoping.productservice.command.data.ProductRepository;

@Component
public class ProductEventsHandler {
    @Autowired
    private ProductRepository productRepository;

    @EventHandler
    public void on(ProductCreateEvent event) {
        Product product = new Product();
        BeanUtils.copyProperties(event, product);
        productRepository.save(product);
    }

    public void on(ProductUpdateEvent event) {
        Optional<Product> oldProduct = productRepository.findById(event.getId());
        if (oldProduct.isPresent()) {
            Product product = oldProduct.get();
            product.setName(event.getName());
            product.setIdCategory(event.getIdCategory());
            product.setDescription(event.getDescription());
            product.setImageUrl(event.getImageUrl());
            product.setPrice(event.getPrice());
            product.setQuantity(event.getQuantity());

            productRepository.save(product);
        }
    }

    public void on(ProductDeleteEvent event) {
        Optional<Product> oldProduct = productRepository.findById(event.getId());
        if (oldProduct.isPresent()) {
            productRepository.deleteById(event.getId());
        }
    }
}
