package com.crm.miniCRM.dto;

import java.time.LocalDate;

public class EventDto {

    private Long id;
    private String description;
    private LocalDate date;
    //  private LocalTime time;

    public EventDto(Long id, String description, LocalDate date) {
        this.id = id;
        this.description = description;
        this.date = date;
        //   this.time = time;
    }

    public EventDto(){}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }
}
