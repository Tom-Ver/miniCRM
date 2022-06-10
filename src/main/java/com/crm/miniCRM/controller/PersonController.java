package com.crm.miniCRM.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

import com.crm.miniCRM.MiniCrmApplication;
import com.crm.miniCRM.dto.PersonDto;
import com.crm.miniCRM.model.Person;
import com.crm.miniCRM.model.persistence.PersonRepository;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import org.junit.platform.commons.function.Try;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

//BRON: //https://attacomsian.com/blog/spring-boot-upload-parse-csv-file
@Controller
@RequestMapping(value = "/persons")
public class PersonController {

    private PersonRepository personService;
    private static final Logger log = LoggerFactory.getLogger(MiniCrmApplication.class);

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
        PersonDto dto = new PersonDto(entity.getId(), entity.getFirstName(), entity.getLastName(), entity.getBirthDay());
         return dto;
    }

    protected Person convertToEntity(PersonDto dto) {
        //29-06-1963
        //int year = Integer.parseInt(dto.getBirthDay().toString().substring(6,10));
        //int month = Integer.parseInt(dto.getBirthDay().toString().substring(3,5));
        //int day = Integer.parseInt(dto.getBirthDay().toString().substring(0,2));
        //DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        //LocalDate birth = LocalDate.parse(dto.getBirthDay(), formatter);
        Person person = new Person(dto.getFirstName(), dto.getLastName(), dto.getBirthDay());
        if (!StringUtils.isEmpty(dto.getId())) {
            person.setId(dto.getId());
        }
        return person;
    }

    //https://attacomsian.com/blog/spring-boot-upload-parse-csv-file
    @GetMapping("/upload")
    public String index() {
        return "person-upload";
    }

    @PostMapping("/upload-csv-file")
    public String uploadCSVFile(@RequestParam("file") MultipartFile file, Model model) {

        // validate file
        if (file.isEmpty()) {
            model.addAttribute("message", "Please select a CSV file to upload.");
            model.addAttribute("status", false);
        } else {

            // parse CSV file to create a list of `User` objects
            try (Reader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {

                // create csv bean reader
                CsvToBean<Person> csvToBean = new CsvToBeanBuilder(reader)
                        .withSkipLines(0)
                        .withSeparator(';')
                        .withType(Person.class)
                        .withIgnoreLeadingWhiteSpace(true)
                        .build();



                // convert `CsvToBean` object to list of users
                List<Person> persons = csvToBean.parse();
                log.info("Log", persons);
                for (Person p: persons) {
                    p.setActive(true);
                }
                personService.saveAll(persons);

                // save users list on model
                model.addAttribute("persons", persons);
                model.addAttribute("status", true);

            } catch (Exception ex) {
                model.addAttribute("message", "An error occurred while processing the CSV file.");
                model.addAttribute("status", false);
            }
        }

        return "file-upload-status";
    }
}
