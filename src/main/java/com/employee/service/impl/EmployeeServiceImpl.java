package com.employee.service.impl;

import com.employee.dto.EmployeeRequestDto;
import com.employee.dto.EmployeeResponseDto;
import com.employee.model.Department;
import com.employee.model.Employee;
import com.employee.model.Section;
import com.employee.repository.DepartmentRepository;
import com.employee.repository.EmployeeRepository;
import com.employee.repository.EmployeeSpecification;
import com.employee.repository.SectionRepository;
import com.employee.service.BaseService;
import com.employee.service.EmployeeService;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class EmployeeServiceImpl extends BaseService implements EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final DepartmentRepository departmentRepository;
    private final SectionRepository sectionRepository;

    public EmployeeServiceImpl(EmployeeRepository employeeRepository,
                               DepartmentRepository departmentRepository,
                               SectionRepository sectionRepository) {
        this.employeeRepository = employeeRepository;
        this.departmentRepository = departmentRepository;
        this.sectionRepository = sectionRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<EmployeeResponseDto> search(Boolean active,
                                            Long deptId,
                                            Long sectionId,
                                            String q,
                                            Pageable pageable) {

        var spec = EmployeeSpecification.search(active, deptId, sectionId, q);
        Page<Employee> page = employeeRepository.findAll(spec, pageable);
        return page.map(this::toResponseDto);
    }

    @Override
    @Transactional(readOnly = true)
    public EmployeeResponseDto get(Long id) {
        Employee employee = findActiveEmployee(id);
        return toResponseDto(employee);
    }

    @Override
    @Transactional
    public EmployeeResponseDto create(EmployeeRequestDto request) {

        if (employeeRepository.existsByEmpNo(request.getEmpNo())) {
            throw new IllegalArgumentException("This employee number has already been used");
        }

        Employee employee = new Employee();
        mapRequest(request, employee);

        Employee saved = employeeRepository.save(employee);
        return toResponseDto(saved);
    }

    @Override
    @Transactional
    public EmployeeResponseDto update(Long id, EmployeeRequestDto request) {

        Employee employee = findActiveEmployee(id);

        if (!employee.getEmpNo().equals(request.getEmpNo()) &&
                employeeRepository.existsByEmpNo(request.getEmpNo())) {
            throw new IllegalArgumentException("This employee number has already been used");
        }

        mapRequest(request, employee);
        Employee saved = employeeRepository.save(employee);
        return toResponseDto(saved);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Employee employee = findActiveEmployee(id);
        employee.setDeleted(true);
        employee.setActive(false);
        employeeRepository.save(employee);
    }

    private Employee findActiveEmployee(Long id) {
        return employeeRepository.findById(id)
                .filter(e -> !e.isDeleted())
                .orElseThrow(() -> new IllegalArgumentException("Employee not found"));
    }

    private void mapRequest(EmployeeRequestDto request, Employee employee) {

        Department department = departmentRepository.findById(request.getDepartmentId())
                .orElseThrow(() -> new IllegalArgumentException("Department not found"));

        Section section = sectionRepository.findById(request.getSectionId())
                .orElseThrow(() -> new IllegalArgumentException("Section not found"));

        if (!section.getDepartment().getId().equals(department.getId())) {
            throw new DataIntegrityViolationException("Section does not belong to department");
        }

        employee.setEmpNo(request.getEmpNo());
        employee.setName(request.getName());
        employee.setDob(request.getDob());
        employee.setEmail(request.getEmail());
        employee.setDepartment(department);
        employee.setSection(section);
        employee.setBasicSalary(request.getBasicSalary());
        employee.setTravelAllowance(nullSafe(request.getTravelAllowance()));
        employee.setOtherAllowance(nullSafe(request.getOtherAllowance()));
        employee.setActive(Optional.ofNullable(request.getActive()).orElse(true));
    }

    private EmployeeResponseDto toResponseDto(Employee employee) {
        EmployeeResponseDto dto = new EmployeeResponseDto();

        dto.setId(employee.getId());
        dto.setEmpNo(employee.getEmpNo());
        dto.setName(employee.getName());
        dto.setDob(employee.getDob());
        dto.setAge(calculateAge(employee.getDob()));
        dto.setEmail(employee.getEmail());

        dto.setDepartmentId(employee.getDepartment().getId());
        dto.setDepartmentName(employee.getDepartment().getName());
        dto.setSectionId(employee.getSection().getId());
        dto.setSectionName(employee.getSection().getName());

        dto.setBasicSalary(employee.getBasicSalary());
        dto.setTravelAllowance(employee.getTravelAllowance());
        dto.setOtherAllowance(employee.getOtherAllowance());
        dto.setTotalSalary(calculateTotalSalary(
                employee.getBasicSalary(),
                employee.getTravelAllowance(),
                employee.getOtherAllowance()
        ));

        dto.setActive(employee.isActive());
        dto.setDeleted(employee.isDeleted());
        dto.setCreatedDatetime(employee.getCreatedDatetime());
        dto.setModifiedDatetime(employee.getModifiedDatetime());
        dto.setCreatedUser(employee.getCreatedUser());
        dto.setModifiedUser(employee.getModifiedUser());

        return dto;
    }
}
