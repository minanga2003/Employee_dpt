package com.employee.service;

import com.employee.model.Section;
import java.util.List;

public interface SectionService {
    List<Section> findByDepartment(Long departmentId);
}
