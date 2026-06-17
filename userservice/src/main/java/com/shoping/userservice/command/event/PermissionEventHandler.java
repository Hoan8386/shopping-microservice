package com.shoping.userservice.command.event;

import java.time.Instant;
import java.util.Optional;

import org.axonframework.eventhandling.EventHandler;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.shoping.userservice.command.data.Permission;
import com.shoping.userservice.command.data.PermissionRepository;

@Component
public class PermissionEventHandler {
    @Autowired
    private PermissionRepository permissionRepository;

    @EventHandler
    public void on(PermissionCreateEvent event) {
        Permission permission = new Permission(
                event.getCode(),
                event.getName(),
                event.getModule(),
                event.getApiPath(),
                event.getMethod());
        permission.setCreatedAt(Instant.now());
        permission.setCreatedBy("system");
        permissionRepository.save(permission);
    }

    @EventHandler
    public void on(PermissionUpdateEvent event) {
        Optional<Permission> oldPermission = permissionRepository.findById(Long.parseLong(event.getId()));
        if (oldPermission.isPresent()) {
            Permission permission = oldPermission.get();
            permission.setCode(event.getCode());
            permission.setName(event.getName());
            permission.setModule(event.getModule());
            permission.setApiPath(event.getApiPath());
            permission.setMethod(event.getMethod());
            permission.setUpdatedAt(Instant.now());
            permission.setUpdatedBy("system");
            permissionRepository.save(permission);
        }
    }

    @EventHandler
    public void on(PermissionDeleteEvent event) {
        Optional<Permission> oldPermission = permissionRepository.findById(Long.parseLong(event.getId()));
        if (oldPermission.isPresent()) {
            permissionRepository.deleteById(Long.parseLong(event.getId()));
        }
    }
}
