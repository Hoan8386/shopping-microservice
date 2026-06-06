package com.shoping.employeeservice.query.specification;

import org.springframework.data.jpa.domain.Specification;

import com.shoping.employeeservice.command.data.Employee;

public class EmployeeSpecification {

    public static Specification<Employee> filterEmployees(
            String firstName,
            String lastName,
            String email,
            Boolean isDisciplined) {

        return (root, query, criteriaBuilder) -> {

            var predicate = criteriaBuilder.conjunction();

            // filter by firstName
            if (firstName != null && !firstName.isEmpty()) {
                predicate = criteriaBuilder.and(
                        predicate,
                        criteriaBuilder.like(
                                criteriaBuilder.lower(root.get("firstName")),
                                "%" + firstName.toLowerCase() + "%"));
            }

            // filter by lastName
            if (lastName != null && !lastName.isEmpty()) {
                predicate = criteriaBuilder.and(
                        predicate,
                        criteriaBuilder.like(
                                criteriaBuilder.lower(root.get("lastName")),
                                "%" + lastName.toLowerCase() + "%"));
            }

            // filter by email
            if (email != null && !email.isEmpty()) {
                predicate = criteriaBuilder.and(
                        predicate,
                        criteriaBuilder.like(
                                criteriaBuilder.lower(root.get("email")),
                                "%" + email.toLowerCase() + "%"));
            }

            // filter by isDisciplined
            if (isDisciplined != null) {
                predicate = criteriaBuilder.and(
                        predicate,
                        criteriaBuilder.equal(root.get("isDisciplined"), isDisciplined));
            }

            return predicate;
        };
    }
}
