package com.shoping.cartservice.command.aggregate;

import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;
import org.springframework.beans.BeanUtils;

import com.shoping.cartservice.command.command.AddItemToCartCommand;
import com.shoping.cartservice.command.command.CreateCartCommand;
import com.shoping.cartservice.command.command.RemoveItemFromCartCommand;
import com.shoping.cartservice.command.command.UpdateCartItemCommand;
import com.shoping.cartservice.command.event.CartClearedEvent;
import com.shoping.cartservice.command.event.CartCreatedEvent;
import com.shoping.cartservice.command.event.CartItemUpdatedEvent;
import com.shoping.cartservice.command.event.ItemAddedToCartEvent;
import com.shoping.cartservice.command.event.ItemRemovedFromCartEvent;
import com.shoping.commonservice.command.ClearCartCommand;
import com.shoping.commonservice.command.RollBackCartCommand;
import com.shoping.cartservice.command.event.CartRolledBackEvent;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Aggregate
@NoArgsConstructor
@Getter
@Setter
public class CartAggregate {

    @AggregateIdentifier
    private String id;

    private String userId;

    @CommandHandler
    public CartAggregate(CreateCartCommand command) {
        CartCreatedEvent event = new CartCreatedEvent();
        BeanUtils.copyProperties(command, event);
        AggregateLifecycle.apply(event);
    }

    @CommandHandler
    public void handle(AddItemToCartCommand command) {
        ItemAddedToCartEvent event = new ItemAddedToCartEvent();
        BeanUtils.copyProperties(command, event);
        AggregateLifecycle.apply(event);
    }

    @CommandHandler
    public void handle(UpdateCartItemCommand command) {
        CartItemUpdatedEvent event = new CartItemUpdatedEvent();
        BeanUtils.copyProperties(command, event);
        AggregateLifecycle.apply(event);
    }

    @CommandHandler
    public void handle(RemoveItemFromCartCommand command) {
        ItemRemovedFromCartEvent event = new ItemRemovedFromCartEvent();
        BeanUtils.copyProperties(command, event);
        AggregateLifecycle.apply(event);
    }

    @CommandHandler
    public void handle(ClearCartCommand command) {
        CartClearedEvent event = new CartClearedEvent();
        BeanUtils.copyProperties(command, event);
        AggregateLifecycle.apply(event);
    }

    @CommandHandler
    public void handle(RollBackCartCommand command) {
        CartRolledBackEvent event = new CartRolledBackEvent(
                this.id,
                command.getUserId(),
                command.getListOrderItems());
        AggregateLifecycle.apply(event);
    }

    @EventSourcingHandler
    public void on(CartCreatedEvent event) {
        this.id = event.getId();
        this.userId = event.getUserId();
    }

    @EventSourcingHandler
    public void on(ItemAddedToCartEvent event) {
        this.id = event.getId();
    }

    @EventSourcingHandler
    public void on(CartItemUpdatedEvent event) {
        this.id = event.getId();
    }

    @EventSourcingHandler
    public void on(ItemRemovedFromCartEvent event) {
        this.id = event.getId();
    }

    @EventSourcingHandler
    public void on(CartClearedEvent event) {
        this.id = event.getId();
    }

    @EventSourcingHandler
    public void on(CartRolledBackEvent event) {
        this.id = event.getId();
        this.userId = event.getUserId();
    }
}
