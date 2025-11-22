package com.employee.service.impl;

import com.employee.dto.EmployeeRequestDto;
import com.employee.dto.EmployeeResponseDto;
import com.employee.dto.PagedEmployeesResponse;
import com.employee.model.Department;
import com.employee.model.Employee;
import com.employee.model.Section;
import com.employee.repository.DepartmentRepository;
import com.employee.repository.EmployeeRepository;
import com.employee.repository.SectionRepository;
import com.employee.service.EmployeeService;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

@Service
public class EmployeeServiceImpl implements EmployeeService {

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
    public PagedEmployeesResponse search(Boolean active, Long departmentId, Long sectionId, String q, int page, int size, String sort) {
        Pageable pageable = PageRequest.of(page, size, buildSort(sort));
        Page<Employee> employees = employeeRepository.search(active, departmentId, sectionId, normalizeQuery(q), pageable);

        List<EmployeeResponseDto> content = employees.getContent().stream()
                .map(this::toResponse)
                .toList();

        BigDecimal pageTotal = content.stream()
                .map(EmployeeResponseDto::getTotalSalary)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        PagedEmployeesResponse response = new PagedEmployeesResponse();
        response.setContent(content);
        response.setPage(employees.getNumber());
        response.setSize(employees.getSize());
        response.setTotalElements(employees.getTotalElements());
        response.setTotalPages(employees.getTotalPages());
        response.setLast(employees.isLast());
        response.setPageTotalSalary(pageTotal);
        return response;
    }

    @Override
    @Transactional(readOnly = true)
    public EmployeeResponseDto get(Long id) {
        Employee employee = findActiveEmployee(id);
        return toResponse(employee);
    }

    @Override
    @Transactional
    public EmployeeResponseDto create(EmployeeRequestDto request) {
        if (employeeRepository.existsByEmpNo(request.getEmpNo())) {
            throw new IllegalArgumentException("This employee number has already been used");
        }

        Employee employee = new Employee();
        mapRequestToEntity(request, employee);

        Employee saved = employeeRepository.save(employee);
        return toResponse(saved);
    }

    @Override
    @Transactional
    public EmployeeResponseDto update(Long id, EmployeeRequestDto request) {
        Employee employee = findActiveEmployee(id);

        if (!employee.getEmpNo().equals(request.getEmpNo()) && employeeRepository.existsByEmpNo(request.getEmpNo())) {
            throw new IllegalArgumentException("This employee number has already been used");
        }

        mapRequestToEntity(request, employee);
        Employee saved = employeeRepository.save(employee);
        return toResponse(saved);
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
        Optional<Employee> employee = employeeRepository.findById(id);
        return employee.filter(e -> !e.isDeleted())
                .orElseThrow(() -> new IllegalArgumentException("Employee not found"));
    }

    private void mapRequestToEntity(EmployeeRequestDto request, Employee employee) {
        Department department = departmentRepository.findById(request.getDepartmentId())
                .orElseThrow(() -> new IllegalArgumentException("Department not found"));

        Section section = sectionRepository.findById(request.getSectionId())
                .orElseThrow(() -> new IllegalArgumentException("Section not found"));

        if (!section.getDepartment().getId().equals(department.getId())) {
            throw new DataIntegrityViolationException("Section does not belong to the specified department");
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

    private EmployeeResponseDto toResponse(Employee employee) {
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
        dto.setTotalSalary(calculateTotalSalary(employee));
        dto.setActive(employee.isActive());
        dto.setDeleted(employee.isDeleted());
        dto.setCreatedDatetime(employee.getCreatedDatetime());
        dto.setModifiedDatetime(employee.getModifiedDatetime());
        dto.setCreatedUser(employee.getCreatedUser());
        dto.setModifiedUser(employee.getModifiedUser());
        return dto;
    }

    private int calculateAge(LocalDate dob) {
        return dob == null ? 0 : Period.between(dob, LocalDate.now()).getYears();
    }

    private BigDecimal calculateTotalSalary(Employee employee) {
        return nullSafe(employee.getBasicSalary())
                .add(nullSafe(employee.getTravelAllowance()))
                .add(nullSafe(employee.getOtherAllowance()));
    }

    private BigDecimal nullSafe(BigDecimal value) {
        return value != null ? value : BigDecimal.ZERO;
    }

    private String normalizeQuery(String query) {
        if (query == null || query.isBlank()) {
            return null;
        }
        return query.toLowerCase(Locale.ROOT).trim();
    }

    private Sort buildSort(String sort) {
        if (sort == null || sort.isBlank()) {
            return Sort.by("empNo").ascending();
        }

        String trimmed = sort.trim();
        Sort.Direction direction = trimmed.startsWith("-") ? Sort.Direction.DESC : Sort.Direction.ASC;
        String property = trimmed.startsWith("-") ? trimmed.substring(1) : trimmed;
        if (property.isBlank()) {
            property = "empNo";
        }
        return Sort.by(direction, property);
    }
}

