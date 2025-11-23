package com.employee.controller;

import com.employee.dto.EmployeeResponseDto;
import com.employee.dto.EmployeeRequestDto;
import com.employee.service.EmployeeService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/employees")
public class EmployeeController {

    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }
    @GetMapping
    public Page<EmployeeResponseDto> search(
            @RequestParam(required = false) Boolean active,
            @RequestParam(required = false) Long deptId,
            @RequestParam(required = false) Long sectionId,
            @RequestParam(required = false) String q,
            Pageable pageable
    ) {
        return employeeService.search(active, deptId, sectionId, q, pageable);
    }

    @GetMapping("/search")
    public Page<EmployeeResponseDto> searchWithPageable(
            @RequestParam(required = false) Boolean active,
            @RequestParam(required = false) Long deptId,
            @RequestParam(required = false) Long sectionId,
            @RequestParam(required = false) String q,
            Pageable pageable
    ) {
        return employeeService.search(active, deptId, sectionId, q, pageable);
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
