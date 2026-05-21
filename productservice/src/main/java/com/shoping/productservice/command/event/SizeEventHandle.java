package com.shoping.productservice.command.event;

import java.util.Optional;

import org.axonframework.eventhandling.EventHandler;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.shoping.productservice.command.data.Size;
import com.shoping.productservice.command.data.SizeRepository;

@Component
public class SizeEventHandle {
    @Autowired
    private SizeRepository sizeRepository;

    @EventHandler
    public void on(SizeCreateEvent event) {
        Size size = new Size();
        BeanUtils.copyProperties(event, size);
        sizeRepository.save(size);
    }

    @EventHandler
    public void on(SizeUpdateEvent event) {
        Optional<Size> oldSize = sizeRepository.findById(event.getId());
        if (oldSize.isPresent()) {
            Size size = oldSize.get();
            size.setName(event.getName());
            size.setDescription(event.getDescription());
            size.setStatus(event.getStatus());
            sizeRepository.save(size);
        }
    }

    @EventHandler
    public void on(SizeDeleteEvent event) {
        Optional<Size> oldSize = sizeRepository.findById(event.getId());
        if (oldSize.isPresent()) {
            sizeRepository.deleteById(event.getId());
        }
    }
}
