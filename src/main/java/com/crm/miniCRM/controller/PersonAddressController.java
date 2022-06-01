package com.crm.miniCRM.controller;


import com.crm.miniCRM.dto.AddressDto;
import com.crm.miniCRM.dto.MemberDto;
import com.crm.miniCRM.dto.PersonAddressDto;
import com.crm.miniCRM.dto.PersonDto;
import com.crm.miniCRM.model.*;
import com.crm.miniCRM.model.persistence.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.*;

@Controller
@RequestMapping(value="/personaddresses")
public class PersonAddressController {
    private PersonAddressRepository personAddressService;
    private PersonRepository personService;
    private AddressRepository addressService;

    public PersonAddressController(PersonAddressRepository personAddressService, PersonRepository personService, AddressRepository addressService){
        this.personAddressService = personAddressService;
        this.personService = personService;
        this.addressService = addressService;
    }

    //List of all persons with their addresses
    @GetMapping
    public String getPersonAddresses(Model model){
        Map<Long,String> personMap = getPersonMap();
        model.addAttribute("personMap", personMap);
        Map<Long, String> addressMap = getAddressMap();
        model.addAttribute("addressMap", addressMap);

        Iterable<PersonAddress> personAddresses = personAddressService.findAll();
        List<PersonAddressDto> personAddressDtos = new ArrayList<>();
        personAddresses.forEach( p -> personAddressDtos.add(convertToDto(p)));
        model.addAttribute("personaddresses", personAddressDtos);
        return "personaddresses";
    }

    @GetMapping("/new")
    public String addPersonWithAddress(Model model){
        List<Person> personList = getActivePersons();
        model.addAttribute("personList", personList);
        List<Address> addressList = getAdresses();
        model.addAttribute(addressList);
        model.addAttribute("personaddress", new PersonAddressDto());
        return "new-personaddress";
    }
    @PostMapping
    public String addPersonAddress(PersonAddressDto personAddressDto) {
        personAddressService.save(convertToEntity(personAddressDto));
        return "redirect:/personaddresses";
    }

    @GetMapping("/edit/{person_ID}/{address_ID}")
    public String editPersonAddress(Model model, @PathVariable Long person_ID, @PathVariable Long address_ID){
        Optional<PersonAddress> optionalPersonAddress = personAddressService.findById(new PersonAddressID(person_ID, address_ID));
        if(optionalPersonAddress.isPresent()){
            PersonAddress personAddress = optionalPersonAddress.get();
            model.addAttribute("personAddress", convertToDto(personAddress));
            model.addAttribute("address", getAddressDescription(personAddress));
            model.addAttribute("name", getPersonName(personAddress));
        }
        return "edit-personaddress";
    }




    protected PersonAddressDto convertToDto(PersonAddress entity) {
        PersonAddressDto dto = new PersonAddressDto(
                entity.getId(),
                entity.getEmail(),
                entity.getPhone(),
                entity.getMobile(),
                entity.getType());
        return dto;
    }

    protected PersonAddress convertToEntity(PersonAddressDto dto){
        PersonAddress personAddress = new PersonAddress(
                new PersonAddressID(dto.getPerson_ID(), dto.getAddress_ID()),
                dto.getEmail(),
                dto.getPhone(),
                dto.getMobile(),
                dto.getType());
        return personAddress;
    }

    protected Map<Long, String> getAddressMap() {
        Map<Long, String> addressMap = new HashMap<>();
        Iterable<Address> addresses = addressService.findAll();
        addresses.forEach(a -> addressMap.put(a.getId(), a.toStringForPersonAddress()));
        return addressMap;
    }

    protected Map<Long, String> getPersonMap(){
        Map<Long,String> personMap = new HashMap<>();
        Iterable<Person> persons = personService.findAll();
        persons.forEach( p -> personMap.put(p.getId(), p.getFirstName() + " " + p.getLastName()));
        return personMap;
    }

    protected String getPersonName(PersonAddress personAddress) {
        String name = null;
        Optional<Person> optionalPerson = personService.findById(personAddress.getId().getPerson_ID());
        if (optionalPerson.isPresent()) {
            name = optionalPerson.get().getFirstName() + " " + optionalPerson.get().getLastName();
        }
        return name;}

    protected String getAddressDescription(PersonAddress personAddress) {
        String description = null;
        Optional<Address> optionalAddress = addressService.findById(personAddress.getId().getAddress_ID());
        if (optionalAddress.isPresent()){
            description = optionalAddress.get().toStringForPersonAddress();
        }
        return description;
    }

    protected List<Person> getActivePersons() {
        List<Person> personList = (List<Person>) personService.findAll();
        List<Person> filteredPersonList = new ArrayList<>();
        personList.forEach(p -> { if(p.getActive()) {filteredPersonList.add(p);}});
        return filteredPersonList;
    }


    protected List<Address> getAdresses() {
        List<Address> addressList = (List<Address>) addressService.findAll();
        return addressList;
    }
}
