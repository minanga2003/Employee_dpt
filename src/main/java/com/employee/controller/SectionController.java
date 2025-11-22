package com.employee.controller;

import com.employee.model.Section;
import com.employee.service.SectionService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/sections")
public class SectionController {

    private final SectionService sectionService;

    public SectionController(SectionService sectionService) {
        this.sectionService = sectionService;
    }

    @GetMapping
    public List<Section> byDepartment(@RequestParam("departmentId") Long departmentId) {
        return sectionService.findByDepartment(departmentId);
    }
}
