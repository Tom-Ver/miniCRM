package com.crm.miniCRM.dto;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

public class PersonDto {

    private Long id;


    private String firstName;
    private String lastName;
    private LocalDate birthDay;



    public PersonDto() {
    }
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public LocalDate getBirthDay() {
        return birthDay;
    }

    public void setBirthDay(LocalDate birthDay) {
        this.birthDay = birthDay;
    }

    public PersonDto(Long id, String firstName, String lastName, LocalDate birthDay) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthDay = birthDay;

    }
}
