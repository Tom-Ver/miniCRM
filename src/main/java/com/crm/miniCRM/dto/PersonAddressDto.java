package com.crm.miniCRM.dto;

import com.crm.miniCRM.model.persistence.PersonAddressID;

public class PersonAddressDto {

    private Long    person_ID;
    private Long    address_ID;
    private String  email;
    private String  phone;
    private String  mobile;
    private char    type;

    public PersonAddressDto(){}

    public PersonAddressDto(PersonAddressID id, String email, String phone, String mobile, char type){
        person_ID = id.getPerson_ID();
        address_ID = id.getAddress_ID();
        this.email = email;
        this.phone = phone;
        this.mobile = mobile;
        this.type = type;
    }

    public Long getPerson_ID() {
        return person_ID;
    }

    public void setPerson_ID(Long person_ID) {
        this.person_ID = person_ID;
    }

    public Long getAddress_ID() {
        return address_ID;
    }

    public void setAddress_ID(Long address_ID) {
        this.address_ID = address_ID;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public char getType() {
        return type;
    }

    public void setType(char type) {
        this.type = type;
    }
}
