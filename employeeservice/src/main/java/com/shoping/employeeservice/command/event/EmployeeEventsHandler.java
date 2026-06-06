package com.shoping.employeeservice.command.event;

import java.util.Optional;

import org.axonframework.eventhandling.EventHandler;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.shoping.employeeservice.command.data.Employee;
import com.shoping.employeeservice.command.data.EmployeeRepository;

@Component
public class EmployeeEventsHandler {

    @Autowired
    private EmployeeRepository employeeRepository;

    @EventHandler
    public void on(EmployeeCreateEvent event) {
        Employee employee = new Employee();
        BeanUtils.copyProperties(event, employee);
        employeeRepository.save(employee);
    }

    @EventHandler
    public void on(EmployeeUpdateEvent event) {
        Optional<Employee> oldEmployee = employeeRepository.findById(event.getId());
        if (oldEmployee.isPresent()) {
            Employee employee = oldEmployee.get();
            employee.setFirstName(event.getFirstName());
            employee.setLastName(event.getLastName());
            employee.setKin(event.getKin());
            employee.setIsDisciplined(event.getIsDisciplined());
            employee.setEmail(event.getEmail());
            employee.setAddress(event.getAddress());
            employee.setAvatar(event.getAvatar());
            employeeRepository.save(employee);
        }
    }

    @EventHandler
    public void on(EmployeeDeleteEvent event) {
        Optional<Employee> oldEmployee = employeeRepository.findById(event.getId());
        if (oldEmployee.isPresent()) {
            employeeRepository.deleteById(event.getId());
        }
    }
}
