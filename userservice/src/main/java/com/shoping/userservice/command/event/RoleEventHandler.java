package com.shoping.userservice.command.event;

import java.time.Instant;
import java.util.Optional;

import org.axonframework.eventhandling.EventHandler;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.shoping.userservice.command.data.Role;
import com.shoping.userservice.command.data.RoleRepository;

@Component
public class RoleEventHandler {
    @Autowired
    private RoleRepository roleRepository;

    @EventHandler
    public void on(RoleCreateEvent event) {
        Role role = new Role();
        role.setName(event.getName());
        role.setDescription(event.getDescription());
        role.setActive(event.isActive());
        role.setCreatedAt(Instant.now());
        role.setCreatedBy("system");
        roleRepository.save(role);
    }

    @EventHandler
    public void on(RoleUpdateEvent event) {
        Optional<Role> oldRole = roleRepository.findById(Long.parseLong(event.getId()));
        if (oldRole.isPresent()) {
            Role role = oldRole.get();
            role.setName(event.getName());
            role.setDescription(event.getDescription());
            role.setActive(event.isActive());
            role.setUpdatedAt(Instant.now());
            role.setUpdatedBy("system");
            roleRepository.save(role);
        }
    }

    @EventHandler
    public void on(RoleDeleteEvent event) {
        Optional<Role> oldRole = roleRepository.findById(Long.parseLong(event.getId()));
        if (oldRole.isPresent()) {
            roleRepository.deleteById(Long.parseLong(event.getId()));
        }
    }
}
