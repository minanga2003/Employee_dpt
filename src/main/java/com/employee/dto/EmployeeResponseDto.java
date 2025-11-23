package com.employee.dto;

import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmployeeResponseDto {

    private Long id;
    private Integer empNo;
    private String name;
    private LocalDate dob;
    private Integer age;
    private String email;
    private Long departmentId;
    private String departmentName;
    private Long sectionId;
    private String sectionName;
    private BigDecimal basicSalary;
    private BigDecimal travelAllowance;
    private BigDecimal otherAllowance;
    private BigDecimal totalSalary;
    private boolean active;
    private boolean deleted;
    private LocalDateTime createdDatetime;
    private LocalDateTime modifiedDatetime;
    private String createdUser;
    private String modifiedUser;
}
