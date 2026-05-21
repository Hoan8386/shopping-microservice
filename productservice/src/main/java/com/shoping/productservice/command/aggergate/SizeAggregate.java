package com.shoping.productservice.command.aggergate;

import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;
import org.springframework.beans.BeanUtils;

import com.shoping.productservice.command.command.SizeCreateCommand;
import com.shoping.productservice.command.command.SizeDeleteCommand;
import com.shoping.productservice.command.command.SizeUpdateCommand;
import com.shoping.productservice.command.event.SizeCreateEvent;
import com.shoping.productservice.command.event.SizeDeleteEvent;
import com.shoping.productservice.command.event.SizeUpdateEvent;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Aggregate
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SizeAggregate {
    @AggregateIdentifier
    private String id;

    private String name;

    private String description;

    private Boolean status;

    @CommandHandler
    public SizeAggregate(SizeCreateCommand command) {
        SizeCreateEvent sizeCreateEvent = new SizeCreateEvent();
        BeanUtils.copyProperties(command, sizeCreateEvent);
        AggregateLifecycle.apply(sizeCreateEvent);
    }

    @CommandHandler
    public SizeAggregate(SizeUpdateCommand command) {
        SizeUpdateEvent sizeUpdateEvent = new SizeUpdateEvent();
        BeanUtils.copyProperties(command, sizeUpdateEvent);
        AggregateLifecycle.apply(sizeUpdateEvent);
    }

    @CommandHandler
    public SizeAggregate(SizeDeleteCommand command) {
        SizeDeleteEvent sizeDeleteEvent = new SizeDeleteEvent();
        BeanUtils.copyProperties(command, sizeDeleteEvent);
        AggregateLifecycle.apply(sizeDeleteEvent);
    }

    @EventSourcingHandler
    public void on(SizeCreateEvent createEvent) {
        this.id = createEvent.getId();
        this.name = createEvent.getName();
        this.description = createEvent.getDescription();
        this.status = createEvent.getStatus();
    }

    @EventSourcingHandler
    public void on(SizeUpdateEvent updateEvent) {
        this.id = updateEvent.getId();
        this.name = updateEvent.getName();
        this.description = updateEvent.getDescription();
        this.status = updateEvent.getStatus();
    }

    @EventSourcingHandler
    public void on(SizeDeleteEvent deleteEvent) {
        this.id = deleteEvent.getId();
    }
}
