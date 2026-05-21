package com.shoping.productservice.command.event;

import java.util.Optional;

import org.axonframework.eventhandling.EventHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.shoping.productservice.command.data.Product;
import com.shoping.productservice.command.data.ProductDetail;
import com.shoping.productservice.command.data.ProductDetailRepository;
import com.shoping.productservice.command.data.ProductRepository;
import com.shoping.productservice.command.data.Size;
import com.shoping.productservice.command.data.SizeRepository;

@Component
public class ProductDetailEventHandler {
    @Autowired
    private ProductDetailRepository productDetailRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private SizeRepository sizeRepository;

    @EventHandler
    public void on(ProductDetailCreateEvent event) {
        Optional<Product> product = productRepository.findById(event.getProductId());
        Optional<Size> size = sizeRepository.findById(event.getSizeId());
        if (product.isEmpty() || size.isEmpty()) {
            return;
        }

        ProductDetail detail = new ProductDetail();
        detail.setId(event.getId());
        detail.setProduct(product.get());
        detail.setSize(size.get());
        detail.setQuantity(event.getQuantity());
        detail.setPrice(event.getPrice());
        detail.setStatus(event.getStatus());
        productDetailRepository.save(detail);
    }

    @EventHandler
    public void on(ProductDetailUpdateEvent event) {
        Optional<ProductDetail> oldDetail = productDetailRepository.findById(event.getId());
        if (oldDetail.isEmpty()) {
            return;
        }

        Optional<Product> product = productRepository.findById(event.getProductId());
        Optional<Size> size = sizeRepository.findById(event.getSizeId());
        if (product.isEmpty() || size.isEmpty()) {
            return;
        }

        ProductDetail detail = oldDetail.get();
        detail.setProduct(product.get());
        detail.setSize(size.get());
        detail.setQuantity(event.getQuantity());
        detail.setPrice(event.getPrice());
        detail.setStatus(event.getStatus());
        productDetailRepository.save(detail);
    }

    @EventHandler
    public void on(ProductDetailDeleteEvent event) {
        Optional<ProductDetail> oldDetail = productDetailRepository.findById(event.getId());
        if (oldDetail.isPresent()) {
            productDetailRepository.deleteById(event.getId());
        }
    }
}
