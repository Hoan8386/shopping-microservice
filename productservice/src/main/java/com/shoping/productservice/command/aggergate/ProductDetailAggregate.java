package com.shoping.productservice.command.aggergate;

import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;
import org.springframework.beans.BeanUtils;

import com.shoping.productservice.command.command.CreateProductDetailCommand;
import com.shoping.productservice.command.command.DeleteProductDetailCommand;
import com.shoping.productservice.command.command.UpdateProductDetailCommand;
import com.shoping.productservice.command.data.ProductRepository;
import com.shoping.productservice.command.data.SizeRepository;
import com.shoping.productservice.command.event.ProductDetailCreateEvent;
import com.shoping.productservice.command.event.ProductDetailDeleteEvent;
import com.shoping.productservice.command.event.ProductDetailUpdateEvent;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Aggregate
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductDetailAggregate {
    @AggregateIdentifier
    private String id;

    private String productId;

    private String sizeId;

    private Integer quantity;

    private Double price;

    private Boolean status;

    @CommandHandler
    public ProductDetailAggregate(
            CreateProductDetailCommand command,
            ProductRepository productRepository,
            SizeRepository sizeRepository) {

        if (!productRepository.existsById(command.getProductId())) {
            throw new RuntimeException("Product not found");
        }

        if (!sizeRepository.existsById(command.getSizeId())) {
            throw new RuntimeException("Size not found");
        }

        ProductDetailCreateEvent event = new ProductDetailCreateEvent();
        BeanUtils.copyProperties(command, event);

        AggregateLifecycle.apply(event);
    }

    @CommandHandler
    public ProductDetailAggregate(UpdateProductDetailCommand command) {
        ProductDetailUpdateEvent updateEvent = new ProductDetailUpdateEvent();
        BeanUtils.copyProperties(command, updateEvent);
        AggregateLifecycle.apply(updateEvent);
    }

    @CommandHandler
    public ProductDetailAggregate(DeleteProductDetailCommand command) {
        ProductDetailDeleteEvent deleteEvent = new ProductDetailDeleteEvent();
        BeanUtils.copyProperties(command, deleteEvent);
        AggregateLifecycle.apply(deleteEvent);
    }

    @EventSourcingHandler
    public void on(ProductDetailCreateEvent event) {
        this.id = event.getId();
        this.productId = event.getProductId();
        this.sizeId = event.getSizeId();
        this.quantity = event.getQuantity();
        this.price = event.getPrice();
        this.status = event.getStatus();
    }

    @EventSourcingHandler
    public void on(ProductDetailUpdateEvent event) {
        this.id = event.getId();
        this.productId = event.getProductId();
        this.sizeId = event.getSizeId();
        this.quantity = event.getQuantity();
        this.price = event.getPrice();
        this.status = event.getStatus();
    }

    @EventSourcingHandler
    public void on(ProductDetailDeleteEvent event) {
        this.id = event.getId();
    }
}
