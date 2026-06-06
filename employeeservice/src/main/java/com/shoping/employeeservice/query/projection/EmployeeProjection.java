package com.shoping.employeeservice.query.projection;

import java.util.ArrayList;
import java.util.List;

import org.axonframework.queryhandling.QueryHandler;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.shoping.employeeservice.command.data.Employee;
import com.shoping.employeeservice.command.data.EmployeeRepository;
import com.shoping.employeeservice.query.model.EmployeeResponseModel;
import com.shoping.employeeservice.query.queries.FilterEmployeeQuery;
import com.shoping.employeeservice.query.queries.GetAllEmployeeQuery;
import com.shoping.employeeservice.query.queries.GetEmployeeDetailQuery;
import com.shoping.employeeservice.query.specification.EmployeeSpecification;

@Component
public class EmployeeProjection {

    @Autowired
    private EmployeeRepository employeeRepository;

    @QueryHandler
    public List<EmployeeResponseModel> handle(GetAllEmployeeQuery query) {
        List<Employee> list = employeeRepository.findAll();
        List<EmployeeResponseModel> result = new ArrayList<>();
        list.forEach(employee -> {
            EmployeeResponseModel model = new EmployeeResponseModel();
            BeanUtils.copyProperties(employee, model);
            result.add(model);
        });
        return result;
    }

    @QueryHandler
    public EmployeeResponseModel handle(GetEmployeeDetailQuery query) {
        EmployeeResponseModel response = new EmployeeResponseModel();
        employeeRepository.findById(query.getId()).ifPresent(
                employee -> BeanUtils.copyProperties(employee, response));
        return response;
    }

    @QueryHandler
    public List<EmployeeResponseModel> handle(FilterEmployeeQuery query) {
        List<EmployeeResponseModel> result = new ArrayList<>();
        List<Employee> employees = employeeRepository.findAll(EmployeeSpecification.filterEmployees(
                query.getFirstName(),
                query.getLastName(),
                query.getEmail(),
                query.getIsDisciplined()));
        employees.forEach(employee -> {
            EmployeeResponseModel item = new EmployeeResponseModel();
            BeanUtils.copyProperties(employee, item);
            result.add(item);
        });
        return result;
    }
}
