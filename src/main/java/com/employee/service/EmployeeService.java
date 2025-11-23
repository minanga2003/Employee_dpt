package com.employee.service;

import com.employee.dto.EmployeeRequestDto;
import com.employee.dto.EmployeeResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface EmployeeService {

    Page<EmployeeResponseDto> search(Boolean active,
                                     Long deptId,
                                     Long sectionId,
                                     String q,
                                     Pageable pageable);

    EmployeeResponseDto get(Long id);

    EmployeeResponseDto create(EmployeeRequestDto request);

    EmployeeResponseDto update(Long id, EmployeeRequestDto request);

    void delete(Long id);
}
