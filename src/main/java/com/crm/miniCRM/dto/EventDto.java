package com.crm.miniCRM.dto;

import com.crm.miniCRM.model.Community;

import java.time.LocalDate;

public class EventDto {

    private Long id;
    private Community community;
    private String description;
    private LocalDate date;
    //  private LocalTime time;

    public EventDto(){}

    public EventDto(Community community, Long id, String description, LocalDate date) {
        this.community = community;
        this.id = id;
        this.description = description;
        this.date = date;
        //   this.time = time;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Community getCommunity() {
        return community;
    }

    public void setCommunity(Community community) {
        this.community = community;
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
