package com.employee.repository;

import com.employee.model.Employee;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    boolean existsByEmpNo(Integer empNo);

    @Query(value = """
            SELECT DISTINCT e FROM Employee e
            LEFT JOIN FETCH e.department
            LEFT JOIN FETCH e.section
            WHERE e.deleted = false
              AND (:active IS NULL OR e.active = :active)
              AND (:deptId IS NULL OR e.department.id = :deptId)
              AND (:sectionId IS NULL OR e.section.id = :sectionId)
              AND (:q IS NULL OR LOWER(e.name) LIKE LOWER(CONCAT('%', :q, '%'))
                             OR LOWER(e.email) LIKE LOWER(CONCAT('%', :q, '%')))
            """,
            countQuery = """
            SELECT COUNT(DISTINCT e) FROM Employee e
            WHERE e.deleted = false
              AND (:active IS NULL OR e.active = :active)
              AND (:deptId IS NULL OR e.department.id = :deptId)
              AND (:sectionId IS NULL OR e.section.id = :sectionId)
              AND (:q IS NULL OR LOWER(e.name) LIKE LOWER(CONCAT('%', :q, '%'))
                             OR LOWER(e.email) LIKE LOWER(CONCAT('%', :q, '%')))
            """)
    Page<Employee> search(@Param("active") Boolean active,
                          @Param("deptId") Long departmentId,
                          @Param("sectionId") Long sectionId,
                          @Param("q") String query,
                          Pageable pageable);
}
