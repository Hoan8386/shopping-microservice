package com.shoping.userservice.command.aggergate;

import java.time.LocalDate;

import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;
import org.springframework.beans.BeanUtils;

import com.shoping.userservice.command.command.CreateUserCommand;
import com.shoping.userservice.command.command.DeleteUserCommand;
import com.shoping.userservice.command.command.UpdateUserCommand;
import com.shoping.userservice.command.event.UserCreateEvent;
import com.shoping.userservice.command.event.UserDeleteEvent;
import com.shoping.userservice.command.event.UserUpdateEvent;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Aggregate
@NoArgsConstructor
@Getter
@Setter
public class UserAggregate {

    @AggregateIdentifier
    private String id;

    private String keycloakId;

    private String fullName;

    private String phone;

    private String address;

    private String avatar;

    private String gender;

    private LocalDate birthday;

    private String status;

    @CommandHandler
    public UserAggregate(CreateUserCommand command) {
        UserCreateEvent userCreateEvent = new UserCreateEvent();
        BeanUtils.copyProperties(command, userCreateEvent);
        AggregateLifecycle.apply(userCreateEvent);
    }

    @CommandHandler
    public void handle(UpdateUserCommand command) {
        UserUpdateEvent userUpdateEvent = new UserUpdateEvent();
        BeanUtils.copyProperties(command, userUpdateEvent);
        AggregateLifecycle.apply(userUpdateEvent);
    }

    @CommandHandler
    public void handle(DeleteUserCommand command) {
        UserDeleteEvent userDeleteEvent = new UserDeleteEvent();
        BeanUtils.copyProperties(command, userDeleteEvent);
        AggregateLifecycle.apply(userDeleteEvent);
    }

    @EventSourcingHandler
    public void on(UserCreateEvent event) {
        this.id = event.getId();
        this.keycloakId = event.getKeycloakId();
        this.fullName = event.getFullName();
        this.phone = event.getPhone();
        this.address = event.getAddress();
        this.avatar = event.getAvatar();
        this.gender = event.getGender();
        this.birthday = event.getBirthday();
        this.status = event.getStatus();
    }

    @EventSourcingHandler
    public void on(UserUpdateEvent event) {
        this.id = event.getId();
        this.keycloakId = event.getKeycloakId();
        this.fullName = event.getFullName();
        this.phone = event.getPhone();
        this.address = event.getAddress();
        this.avatar = event.getAvatar();
        this.gender = event.getGender();
        this.birthday = event.getBirthday();
        this.status = event.getStatus();
    }

    @EventSourcingHandler
    public void on(UserDeleteEvent event) {
        this.id = event.getId();
    }
}
