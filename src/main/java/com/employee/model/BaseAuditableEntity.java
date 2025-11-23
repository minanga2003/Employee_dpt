package com.employee.model;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;

import java.time.LocalDateTime;

@MappedSuperclass
public abstract class BaseAuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "created_datetime", nullable = false, updatable = false)
    private LocalDateTime createdDatetime;

    @Column(name = "modified_datetime")
    private LocalDateTime modifiedDatetime;

    @Column(name = "created_user", updatable = false)
    private String createdUser;

    @Column(name = "modified_user")
    private String modifiedUser;

    @PrePersist
    private void handlePrePersist() {
        LocalDateTime now = LocalDateTime.now();
        createdDatetime = now;
        modifiedDatetime = null;
        createdUser = createdUser != null ? createdUser : "system";
        modifiedUser = null;
        onPrePersist();
    }

    @PreUpdate
    private void handlePreUpdate() {
        modifiedDatetime = LocalDateTime.now();
        if (modifiedUser == null) {
            modifiedUser = "system";
        }
        onPreUpdate();
    }
    protected void onPrePersist() {

    }
    protected void onPreUpdate() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getCreatedDatetime() {
        return createdDatetime;
    }

    public void setCreatedDatetime(LocalDateTime createdDatetime) {
        this.createdDatetime = createdDatetime;
    }

    public LocalDateTime getModifiedDatetime() {
        return modifiedDatetime;
    }

    public void setModifiedDatetime(LocalDateTime modifiedDatetime) {
        this.modifiedDatetime = modifiedDatetime;
    }

    public String getCreatedUser() {
        return createdUser;
    }

    public void setCreatedUser(String createdUser) {
        this.createdUser = createdUser;
    }

    public String getModifiedUser() {
        return modifiedUser;
    }

    public void setModifiedUser(String modifiedUser) {
        this.modifiedUser = modifiedUser;
    }
}