package com.employee.controller;

import com.employee.dto.EmployeeRequestDto;
import com.employee.dto.EmployeeResponseDto;
import com.employee.dto.PagedEmployeesResponse;
import com.employee.service.EmployeeService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/employees")
public class EmployeeController {

    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @GetMapping
    public PagedEmployeesResponse search(@RequestParam(value = "active", required = false) Boolean active,
                                         @RequestParam(value = "departmentId", required = false) Long departmentId,
                                         @RequestParam(value = "sectionId", required = false) Long sectionId,
                                         @RequestParam(value = "q", required = false) String q,
                                         @RequestParam(value = "page", defaultValue = "0") int page,
                                         @RequestParam(value = "size", defaultValue = "10") int size,
                                         @RequestParam(value = "sort", defaultValue = "empNo") String sort) {
        return employeeService.search(active, departmentId, sectionId, q, page, size, sort);
    }

    @GetMapping("/{id}")
    public EmployeeResponseDto get(@PathVariable Long id) {
        return employeeService.get(id);
    }

    @PostMapping
    public EmployeeResponseDto create(@Valid @RequestBody EmployeeRequestDto request) {
        return employeeService.create(request);
    }

    @PutMapping("/{id}")
    public EmployeeResponseDto update(@PathVariable Long id, @Valid @RequestBody EmployeeRequestDto request) {
        return employeeService.update(id, request);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        employeeService.delete(id);
    }
}
