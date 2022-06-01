package com.crm.miniCRM.controller;


import com.crm.miniCRM.dto.PersonAddressDto;
import com.crm.miniCRM.model.Address;
import com.crm.miniCRM.model.Person;
import com.crm.miniCRM.model.PersonAddress;
import com.crm.miniCRM.model.persistence.AddressRepository;
import com.crm.miniCRM.model.persistence.PersonAddressRepository;
import com.crm.miniCRM.model.persistence.PersonRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(value="/personaddress")
public class PersonAddressController {
    private PersonAddressRepository personAddressService;
    private PersonRepository personService;
    private AddressRepository addressService;

    public PersonAddressController(PersonAddressRepository personAddressService, PersonRepository personService, AddressRepository addressService){
        this.personAddressService = personAddressService;
        this.personService = personService;
        this.addressService = addressService;
    }

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


    protected PersonAddressDto convertToDto(PersonAddress entity) {
        PersonAddressDto dto = new PersonAddressDto(
                entity.getId(),
                entity.getEmail(),
                entity.getPhone(),
                entity.getMobile(),
                entity.getType());
        return dto;
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

}
