package com.employee.dto;

import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmployeeRequestDto {

    @NotNull
    private Integer empNo;

    @NotBlank
    private String name;

    @NotNull
    private LocalDate dob;

    @Email
    private String email;

    @NotNull
    private Long departmentId;

    @NotNull
    private Long sectionId;

    @NotNull
    @DecimalMin("0.0")
    private BigDecimal basicSalary;

    @DecimalMin("0.0")
    private BigDecimal travelAllowance;

    @DecimalMin("0.0")
    private BigDecimal otherAllowance;

    private Boolean active;
}
