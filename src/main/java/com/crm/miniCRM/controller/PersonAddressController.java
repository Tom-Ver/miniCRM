package com.crm.miniCRM.controller;


import com.crm.miniCRM.dto.PersonAddressDto;
import com.crm.miniCRM.model.*;
import com.crm.miniCRM.model.persistence.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

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

    //List of all active persons with their addresses
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

    //Add an already known person with an already known address
    @GetMapping("/new")
    public String addPersonWithAddress(Model model){
        List<Person> personList = getActivePersons();
        model.addAttribute("personList", personList);
        List<Address> addressList = getAdresses();
        model.addAttribute(addressList);
        model.addAttribute("personaddress", new PersonAddressDto());
        return "new-personaddress";
    }
    /*@PostMapping
    public String addPersonAddress(PersonAddressDto personAddressDto) {
        personAddressService.save(convertToEntity(personAddressDto));
        return "redirect:/personaddresses";
    }*/

    //Edit the PersonAddress
    @GetMapping("/edit/{person_ID}/{address_ID}")
    public String editPersonAddress(Model model, @PathVariable Long person_ID, @PathVariable Long address_ID){
        Optional<PersonAddress> optionalPersonAddress = personAddressService.findById(new PersonAddressID(person_ID, address_ID));
        List<Address> addressList = getAdresses();
        model.addAttribute(addressList);
        if(optionalPersonAddress.isPresent()){
            PersonAddress personAddress = optionalPersonAddress.get();
            model.addAttribute("personAddress", convertToDto(personAddress));
            model.addAttribute("name", getPersonName(personAddress));
        }
        return "edit-personaddress";
    }
    @PostMapping
    public String editPersonAddress(PersonAddressDto dto){
    Iterable<PersonAddress> personAddresses = personAddressService.findByPersonId(dto.getPerson_ID());
    if(personAddresses.iterator().hasNext()){
    PersonAddress personAddressToEdit = personAddresses.iterator().next();
    personAddressService.delete(personAddressToEdit);}
    PersonAddress personAddressEdited = convertToEntity(dto);
    personAddressService.save(personAddressEdited);
    return "redirect:/personaddresses";
    }

    @RequestMapping(path = "/report", method = RequestMethod.GET)
    public ModelAndView report() {
        Map<String, Object> model = new HashMap<>();
        Iterable<PersonAddress> personAddresses = personAddressService.findAll();
        model.put("personaddress", personAddresses);
        Map<Long,String> personMap = getPersonMap();
        model.put("personMap", personMap);
        Map<Long, String> addressMap = getAddressLabelMap();
        model.put("addressMap", addressMap);
        return new ModelAndView(new LabelPdfView(), model);
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

    protected Map<Long, String> getAddressLabelMap() {
        Map<Long, String> addressMap = new HashMap<>();
        Iterable<Address> addresses = addressService.findAll();
        addresses.forEach(a -> addressMap.put(a.getId(), a.toStringForLabelsPersonAddress()));
        return addressMap;
    }

    protected Map<Long, String> getPersonMap(){
        Map<Long,String> personMap = new HashMap<>();
        Iterable<Person> persons = personService.findAll();
        persons.forEach( p -> {
            if(p.getActive()){
                personMap.put(p.getId(), p.getFirstName() + " " + p.getLastName());
            }
        });
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
