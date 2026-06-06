package com.shoping.employeeservice.query.controller;

import java.util.List;

import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.shoping.commonservice.util.anotation.ApiMessage;
import com.shoping.employeeservice.query.model.EmployeeResponseModel;
import com.shoping.employeeservice.query.queries.FilterEmployeeQuery;
import com.shoping.employeeservice.query.queries.GetAllEmployeeQuery;
import com.shoping.employeeservice.query.queries.GetEmployeeDetailQuery;

// Query flow:
// 1. EmployeeQueryController receives HTTP GET and sends query via QueryGateway.
// 2. GetAllEmployeeQuery / GetEmployeeDetailQuery are the query markers.
// 3. EmployeeProjection handles the queries with @QueryHandler.
// 4. EmployeeResponseModel is the DTO returned to the API consumer.

@RestController
@RequestMapping("/api/v1/employees")
public class EmployeeQueryController {

    @Autowired
    private QueryGateway queryGateway;

    @GetMapping
    @ApiMessage("Get all employees")
    public ResponseEntity<List<EmployeeResponseModel>> getAllEmployees() {
        GetAllEmployeeQuery query = new GetAllEmployeeQuery();
        List<EmployeeResponseModel> result = queryGateway
                .query(query, ResponseTypes.multipleInstancesOf(EmployeeResponseModel.class))
                .join();
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @GetMapping("/{employeeId}")
    @ApiMessage("Get employee detail")
    public ResponseEntity<EmployeeResponseModel> getEmployeeDetail(@PathVariable String employeeId) {
        GetEmployeeDetailQuery query = new GetEmployeeDetailQuery(employeeId);
        EmployeeResponseModel result = queryGateway
                .query(query, ResponseTypes.instanceOf(EmployeeResponseModel.class))
                .join();
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @GetMapping("/filter")
    @ApiMessage("Filter employees")
    public ResponseEntity<List<EmployeeResponseModel>> filterEmployees(
            @RequestParam(required = false) String firstName,
            @RequestParam(required = false) String lastName,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) Boolean isDisciplined) {
        FilterEmployeeQuery query = new FilterEmployeeQuery(firstName, lastName, email, isDisciplined);
        List<EmployeeResponseModel> result = queryGateway
                .query(query, ResponseTypes.multipleInstancesOf(EmployeeResponseModel.class))
                .join();
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }
}
