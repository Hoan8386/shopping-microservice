package com.shoping.orderservice.command.command;

import org.axonframework.modelling.command.TargetAggregateIdentifier;

public class OrderDeleteCommand {
    @TargetAggregateIdentifier
    private String id;

    
}
