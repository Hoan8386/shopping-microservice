package com.shoping.employeeservice.command.aggregate;

import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;
import org.springframework.beans.BeanUtils;

import com.shoping.employeeservice.command.command.CreateEmployeeCommand;
import com.shoping.employeeservice.command.command.DeleteEmployeeCommand;
import com.shoping.employeeservice.command.command.UpdateEmployeeCommand;
import com.shoping.employeeservice.command.event.EmployeeCreateEvent;
import com.shoping.employeeservice.command.event.EmployeeDeleteEvent;
import com.shoping.employeeservice.command.event.EmployeeUpdateEvent;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Aggregate
@NoArgsConstructor
@Getter
@Setter
public class EmployeeAggregate {

    @AggregateIdentifier
    private String id;

    private String firstName;

    private String lastName;

    private String kin;

    private Boolean isDisciplined;

    private String email;

    private String address;

    private String avatar;

    // ─── Command Handlers ─────────────────────────────────────────────────────

    @CommandHandler
    public EmployeeAggregate(CreateEmployeeCommand command) {

        EmployeeCreateEvent event = new EmployeeCreateEvent();
        BeanUtils.copyProperties(command, event);
        AggregateLifecycle.apply(event);

    }

    @CommandHandler
    public void handle(UpdateEmployeeCommand command) {
        EmployeeUpdateEvent event = new EmployeeUpdateEvent();
        BeanUtils.copyProperties(command, event);
        AggregateLifecycle.apply(event);
    }

    @CommandHandler
    public void handle(DeleteEmployeeCommand command) {
        EmployeeDeleteEvent event = new EmployeeDeleteEvent();
        BeanUtils.copyProperties(command, event);
        AggregateLifecycle.apply(event);
    }

    // ─── Event Sourcing Handlers ───────────────────────────────────────────────

    @EventSourcingHandler
    public void on(EmployeeCreateEvent event) {
        this.id = event.getId();
        this.firstName = event.getFirstName();
        this.lastName = event.getLastName();
        this.kin = event.getKin();
        this.isDisciplined = event.getIsDisciplined();
        this.email = event.getEmail();
        this.address = event.getAddress();
        this.avatar = event.getAvatar();
    }

    @EventSourcingHandler
    public void on(EmployeeUpdateEvent event) {
        this.id = event.getId();
        this.firstName = event.getFirstName();
        this.lastName = event.getLastName();
        this.kin = event.getKin();
        this.isDisciplined = event.getIsDisciplined();
        this.email = event.getEmail();
        this.address = event.getAddress();
        this.avatar = event.getAvatar();
    }

    @EventSourcingHandler
    public void on(EmployeeDeleteEvent event) {
        this.id = event.getId();
    }
}
