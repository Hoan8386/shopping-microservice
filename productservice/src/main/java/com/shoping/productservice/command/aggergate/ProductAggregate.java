package com.shoping.productservice.command.aggergate;

import java.math.BigDecimal;

import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;
import org.springframework.beans.BeanUtils;

import com.shoping.productservice.command.command.CreateProductCommand;
import com.shoping.productservice.command.command.DeleteProductCommand;
import com.shoping.productservice.command.command.UpdateProductCommand;
import com.shoping.productservice.command.event.ProductCreateEvent;
import com.shoping.productservice.command.event.ProductUpdateEvent;
import com.shoping.productservice.command.event.ProductDeleteEvent;

import ch.qos.logback.core.joran.util.beans.BeanUtil;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Aggregate
@NoArgsConstructor
@Getter
@Setter
public class ProductAggregate {

    @AggregateIdentifier
    private String Id;

    private String name;

    private String description;

    private Double price;

    private Integer quantity;

    private String idCategory;

    private String imageUrl;

    @CommandHandler
    public ProductAggregate(CreateProductCommand command) {
        ProductCreateEvent productCreateEvent = new ProductCreateEvent();
        BeanUtils.copyProperties(command, productCreateEvent);
        AggregateLifecycle.apply(productCreateEvent);
    }

    @CommandHandler
    public ProductAggregate(UpdateProductCommand command) {
        ProductUpdateEvent productUpdateEvent = new ProductUpdateEvent();
        BeanUtils.copyProperties(command, productUpdateEvent);
        AggregateLifecycle.apply(productUpdateEvent);
    }

    @CommandHandler
    public ProductAggregate(DeleteProductCommand command) {
        ProductDeleteEvent ProductDeleteEvent = new ProductDeleteEvent();
        BeanUtils.copyProperties(command, ProductDeleteEvent);
        AggregateLifecycle.apply(ProductDeleteEvent);
    }

    @EventSourcingHandler
    public void on(ProductCreateEvent productCreateEvent) {
        this.Id = productCreateEvent.getId();
        this.name = productCreateEvent.getName();
        this.idCategory = productCreateEvent.getIdCategory();
        this.description = productCreateEvent.getDescription();
        this.price = productCreateEvent.getPrice();
        this.quantity = productCreateEvent.getQuantity();
        this.imageUrl = productCreateEvent.getImageUrl();
    }

    @EventSourcingHandler
    public void on(ProductUpdateEvent productCreateEvent) {
        this.Id = productCreateEvent.getId();
        this.name = productCreateEvent.getName();
        this.idCategory = productCreateEvent.getIdCategory();
        this.description = productCreateEvent.getDescription();
        this.price = productCreateEvent.getPrice();
        this.quantity = productCreateEvent.getQuantity();
        this.imageUrl = productCreateEvent.getImageUrl();
    }

    @EventSourcingHandler
    public void on(ProductDeleteEvent productCreateEvent) {
        this.Id = productCreateEvent.getId();

    }
}
