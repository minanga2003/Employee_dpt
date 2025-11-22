package com.employee.service.impl;

import com.employee.model.Section;
import com.employee.repository.SectionRepository;
import com.employee.service.SectionService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SectionServiceImpl implements SectionService {

    private final SectionRepository sectionRepository;

    public SectionServiceImpl(SectionRepository sectionRepository) {
        this.sectionRepository = sectionRepository;
    }

    @Override
    public List<Section> findByDepartment(Long departmentId) {
        return sectionRepository.findByDepartmentId(departmentId);
    }
}

