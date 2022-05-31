package com.crm.miniCRM.model;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name="event")
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name="community_id")
    private Community community;
    private String description;
    private LocalDate date;
    private Boolean active;

    //  private LocalTime time;

    public Event(){}
    public Event(Community community, String description, LocalDate date) {
        this.community = community;
        this.description = description;
        this.date = date;
        this.active = true;
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

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    //    public LocalTime getTime() {
//        return time;
//    }
//
//    public void setTime(LocalTime time) {
//        this.time = time;
//    }

    @Override
    public String toString() {
        return "Event{" +
                "ID=" + id +
                ", community='" + community + '\'' +
                ", description='" + description + '\'' +
                ", date=" + date +
                //", time=" + time +
                '}';
    }
}
