package com.employee.repository;

import com.employee.model.Employee;
import jakarta.persistence.criteria.JoinType;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class EmployeeSpecification {

    public static Specification<Employee> search(
            Boolean active,
            Long deptId,
            Long sectionId,
            String q
    ) {
        return (root, query, cb) -> {
            query.distinct(true);
            root.fetch("department", JoinType.LEFT);
            root.fetch("section", JoinType.LEFT);

            List<jakarta.persistence.criteria.Predicate> predicates = new ArrayList<>();
            predicates.add(cb.isFalse(root.get("deleted")));

            if (active != null) {
                predicates.add(cb.equal(root.get("active"), active));
            }

            if (deptId != null) {
                predicates.add(cb.equal(root.get("department").get("id"), deptId));
            }

            if (sectionId != null) {
                predicates.add(cb.equal(root.get("section").get("id"), sectionId));
            }

            if (q != null && !q.isBlank()) {
                String pattern = "%" + q.toLowerCase() + "%";

                predicates.add(
                        cb.or(
                                cb.like(cb.lower(root.get("name")), pattern),
                                cb.like(cb.lower(root.get("email")), pattern)
                        )
                );
            }

            return cb.and(predicates.toArray(new jakarta.persistence.criteria.Predicate[0]));
        };
    }
}
