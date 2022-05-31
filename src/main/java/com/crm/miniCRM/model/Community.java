package com.crm.miniCRM.model;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name="community")
public class Community {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ID;

    @ManyToMany(mappedBy = "member")
    private List<Person> member;

    @OneToMany(mappedBy = "community")
    private List<Event> events;
    private String description;
    private Boolean active;

    public Community(){}

    public Community(String description) {
        this.description = description;
        this.active = true;
    }

    public Long getID() {
        return ID;
    }

    public void setID(Long ID) {
        this.ID = ID;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    @Override
    public String toString() {
        return "Community{" +
                "ID=" + ID +
                ", description='" + description + '\'' +
                '}';
    }
}
