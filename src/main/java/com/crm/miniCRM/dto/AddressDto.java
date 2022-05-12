package com.crm.miniCRM.dto;

public class AddressDto {

    //Variables
    private Long id;
    private String street;
    private String number;
    private String box;
    private String zip;
    private String city;
    private String country;
    private String type;

    //Constructors
    public AddressDto(Long id, String street, String number, String box, String zip, String city, String country, String type) {
        this.id = id;
        this.street = street;
        this.number = number;
        this.box = box;
        this.zip = zip;
        this.city = city;
        this.country = country;
        this.type = type;
    }
    public AddressDto(){}

    //Getters & Setters


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public String getStreet() {
        return street;
    }
    public void setStreet(String street) {
        this.street = street;
    }
    public String getNumber() {
        return number;
    }
    public void setNumber(String number) {
        this.number = number;
    }
    public String getBox() {
        return box;
    }
    public void setBox(String box) {
        this.box = box;
    }
    public String getZip() {
        return zip;
    }
    public void setZip(String zip) {
        this.zip = zip;
    }
    public String getCity() {
        return city;
    }
    public void setCity(String city) {
        this.city = city;
    }
    public String getCountry() {
        return country;
    }
    public void setCountry(String country) {
        this.country = country;
    }
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }
}
