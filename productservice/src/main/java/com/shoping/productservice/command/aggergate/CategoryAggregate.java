package com.shoping.productservice.command.aggergate;

import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;
import org.springframework.beans.BeanUtils;

import com.shoping.productservice.command.command.CreateCategoryCommand;
import com.shoping.productservice.command.command.DeleteCategoryCommand;
import com.shoping.productservice.command.command.UpdateCategoryCommand;
import com.shoping.productservice.command.event.CategoryCreateEvent;
import com.shoping.productservice.command.event.CategoryDeleteEvent;
import com.shoping.productservice.command.event.CategoryUpdateEvent;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Aggregate
@NoArgsConstructor
@Getter
@Setter
public class CategoryAggregate {
    @AggregateIdentifier

    private String id;

    private String name;

    private String description;

    private Boolean status;

    @CommandHandler
    public CategoryAggregate(CreateCategoryCommand command) {
        CategoryCreateEvent event = new CategoryCreateEvent();
        BeanUtils.copyProperties(command, event);
        AggregateLifecycle.apply(event);
    }

    @CommandHandler
    public void handle(UpdateCategoryCommand command) {
        CategoryUpdateEvent event = new CategoryUpdateEvent();
        BeanUtils.copyProperties(command, event);
        AggregateLifecycle.apply(event);
    }

    @CommandHandler
    public void handle(DeleteCategoryCommand command) {
        CategoryDeleteEvent event = new CategoryDeleteEvent();
        BeanUtils.copyProperties(command, event);
        AggregateLifecycle.apply(event);
    }

    @EventSourcingHandler
    public void on(CategoryCreateEvent categoryCreateEvent) {
        this.id = categoryCreateEvent.getId();
        this.name = categoryCreateEvent.getName();
        this.description = categoryCreateEvent.getDescription();
        this.status = categoryCreateEvent.getStatus();
    }

    @EventSourcingHandler
    public void on(CategoryUpdateEvent categoryUpdateEvent) {
        this.id = categoryUpdateEvent.getId();
        this.name = categoryUpdateEvent.getName();
        this.description = categoryUpdateEvent.getDescription();
        this.status = categoryUpdateEvent.getStatus();
    }

    @EventSourcingHandler
    public void on(CategoryDeleteEvent categoryDeleteEvent) {
        this.id = categoryDeleteEvent.getId();

    }
}
