package com.crm.miniCRM.controller;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

import com.crm.miniCRM.dto.PersonDto;
import com.crm.miniCRM.model.Person;
import com.crm.miniCRM.model.persistence.PersonRepository;
import org.junit.platform.commons.function.Try;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequestMapping(value = "/persons")
public class PersonController {

    private PersonRepository personService;

    public PersonController(PersonRepository personService) {
        this.personService = personService;
    }

    @GetMapping
    public String getPersons(Model model) {
        Iterable<Person> persons = personService.findAll();
        List<PersonDto> personDtos = new ArrayList<>();
        for (Person p : persons) {
            if(p.getActive()){
                personDtos.add(convertToDto(p));
            }
        }
        model.addAttribute("persons", personDtos);
        return "persons";
    }



    @GetMapping("/new")
    public String newperson(Model model) {
        model.addAttribute("person", new PersonDto());
        return "new-person";
    }

    @PostMapping
    public String addperson(PersonDto person) {
        personService.save(convertToEntity(person));

        return "redirect:/persons";
    }

    @GetMapping("edit/{person_ID}")
    public String editPerson(Model model, @PathVariable Long person_ID){
        Optional<Person> personOptional = personService.findById(person_ID);
        if(personOptional.isPresent()){
            Person person = personOptional.get();
            model.addAttribute("person", person);
        }
        return "edit-person";
    }

    @GetMapping("delete/{person_ID}")
    public String deletePerson(Model model, @PathVariable Long person_ID) {
        Optional<Person> personOptional = personService.findById(person_ID);
        if(personOptional.isPresent()){
            Person person = personOptional.get();
            model.addAttribute("person", convertToDto(person));
        }
        return "delete-person";

    }
    @PostMapping("/delete")
    public String deletePerson(PersonDto personDto){
        Person deletePerson = convertToEntity(personDto);
        deletePerson.setActive(false);
        personService.save(deletePerson);

        return "redirect:/persons";
    }

    protected PersonDto convertToDto(Person entity) {
        PersonDto dto = new PersonDto(entity.getId(), entity.getFirstName(), entity.getLastName(), entity.getBirthDay().toString());
         return dto;
    }

    protected Person convertToEntity(PersonDto dto) {
        //29-06-1963
        //int year = Integer.parseInt(dto.getBirthDay().toString().substring(6,10));
        //int month = Integer.parseInt(dto.getBirthDay().toString().substring(3,5));
        //int day = Integer.parseInt(dto.getBirthDay().toString().substring(0,2));
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate birth = LocalDate.parse(dto.getBirthDay(), formatter);
        Person person = new Person(dto.getFirstName(), dto.getLastName(), birth);
        if (!StringUtils.isEmpty(dto.getId())) {
            person.setId(dto.getId());
        }
        return person;
    }



}
