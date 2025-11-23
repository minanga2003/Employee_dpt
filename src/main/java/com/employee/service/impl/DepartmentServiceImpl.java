package com.employee.service.impl;

import com.employee.model.Department;
import com.employee.repository.DepartmentRepository;
import com.employee.service.DepartmentService;
import com.employee.service.BaseService;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class DepartmentServiceImpl extends BaseService implements DepartmentService {

    private final DepartmentRepository departmentRepository;

    public DepartmentServiceImpl(DepartmentRepository departmentRepository) {
        this.departmentRepository = departmentRepository;
    }

    @Override
    public List<Department> findAll() {
        return departmentRepository.findAll();
    }
}
