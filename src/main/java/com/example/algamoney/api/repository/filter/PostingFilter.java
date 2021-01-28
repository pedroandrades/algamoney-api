package com.example.algamoney.api.repository.filter;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDate;

public class PostingFilter {

    private String description;

    @JsonFormat(pattern =  "dd/MM/yyyy")
    private LocalDate dueDateBefore;

    @JsonFormat(pattern =  "dd/MM/yyyy")
    private LocalDate dueDateAfter;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getDueDateBefore() {
        return dueDateBefore;
    }

    public void setDueDateBefore(LocalDate dueDateBefore) {
        this.dueDateBefore = dueDateBefore;
    }

    public LocalDate getDueDateAfter() {
        return dueDateAfter;
    }

    public void setDueDateAfter(LocalDate dueDateAfter) {
        this.dueDateAfter = dueDateAfter;
    }
}
