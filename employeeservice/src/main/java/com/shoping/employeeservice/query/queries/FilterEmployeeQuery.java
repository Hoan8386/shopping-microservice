package com.shoping.employeeservice.query.queries;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FilterEmployeeQuery {
    private String firstName;
    private String lastName;
    private String email;
    private Boolean isDisciplined;
}
