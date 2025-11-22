package com.employee.service;

import com.employee.dto.EmployeeRequestDto;
import com.employee.dto.EmployeeResponseDto;
import com.employee.dto.PagedEmployeesResponse;

public interface EmployeeService {

    PagedEmployeesResponse search(Boolean active, Long departmentId, Long sectionId, String q, int page, int size, String sort);

    EmployeeResponseDto get(Long id);

    EmployeeResponseDto create(EmployeeRequestDto request);

    EmployeeResponseDto update(Long id, EmployeeRequestDto request);

    void delete(Long id);
}
