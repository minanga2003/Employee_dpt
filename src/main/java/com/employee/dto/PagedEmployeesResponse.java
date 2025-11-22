package com.employee.dto;

import java.math.BigDecimal;
import java.util.List;

public class PagedEmployeesResponse {

    private List<EmployeeResponseDto> content;
    private int page;
    private int size;
    private long totalElements;
    private int totalPages;
    private boolean last;
    private BigDecimal pageTotalSalary;

    public List<EmployeeResponseDto> getContent() {
        return content;
    }

    public void setContent(List<EmployeeResponseDto> content) {
        this.content = content;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public long getTotalElements() {
        return totalElements;
    }

    public void setTotalElements(long totalElements) {
        this.totalElements = totalElements;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public boolean isLast() {
        return last;
    }

    public void setLast(boolean last) {
        this.last = last;
    }

    public BigDecimal getPageTotalSalary() {
        return pageTotalSalary;
    }

    public void setPageTotalSalary(BigDecimal pageTotalSalary) {
        this.pageTotalSalary = pageTotalSalary;
    }
}
