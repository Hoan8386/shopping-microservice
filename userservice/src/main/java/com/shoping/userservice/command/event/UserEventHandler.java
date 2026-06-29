package com.shoping.userservice.command.event;

import java.time.Instant;
import java.util.Optional;

import org.axonframework.eventhandling.EventHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.shoping.userservice.command.data.User;
import com.shoping.userservice.command.data.UserRepository;

@Component
public class UserEventHandler {

    @Autowired
    private UserRepository userRepository;

    @EventHandler
    public void on(UserCreateEvent event) {
        User user = new User();
        user.setId(event.getId());
        user.setKeycloakId(event.getKeycloakId());
        user.setFullName(event.getFullName());
        user.setPhone(event.getPhone());
        user.setAddress(event.getAddress());
        user.setAvatar(event.getAvatar());
        user.setGender(event.getGender());
        user.setBirthday(event.getBirthday());
        user.setStatus(event.getStatus());
        user.setCreatedAt(Instant.now());
        userRepository.save(user);
    }

    @EventHandler
    public void on(UserUpdateEvent event) {
        Optional<User> existingUser = userRepository.findById(event.getId());
        if (existingUser.isPresent()) {
            User user = existingUser.get();
            user.setKeycloakId(event.getKeycloakId());
            user.setFullName(event.getFullName());
            user.setPhone(event.getPhone());
            user.setAddress(event.getAddress());
            user.setAvatar(event.getAvatar());
            user.setGender(event.getGender());
            user.setBirthday(event.getBirthday());
            user.setStatus(event.getStatus());
            user.setUpdatedAt(Instant.now());
            userRepository.save(user);
        }
    }

    @EventHandler
    public void on(UserDeleteEvent event) {
        Optional<User> existingUser = userRepository.findById(event.getId());
        if (existingUser.isPresent()) {
            userRepository.deleteById(event.getId());
        }
    }
}
