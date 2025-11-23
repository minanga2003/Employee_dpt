package com.employee.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.PostLoad;
import jakarta.persistence.Table;

@Entity
@Table(name = "departments")
public class Department extends BaseAuditableEntity {

    @Column(nullable = false, unique = true, length = 100)
    private String name;
    @Column(nullable = false)
    private Integer status = 1;
    @PostLoad
    private void ensureStatus() {
        if (status == null) {
            status = 1;
        }
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}