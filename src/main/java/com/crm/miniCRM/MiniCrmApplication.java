package com.crm.miniCRM;

//https://spring.io/guides/gs/accessing-data-jpa/

/*
* Title:    Exam Assignment Java Gevorderd: MiniCrm
* Author:   Klara Craninx & Tom Vervaeren (Base project by Paesen Mathy)
* School:   UC Leuven-Limburg Campus Proximus
* Lecturer: Paesen Mathy
 */

import com.crm.miniCRM.model.*;
import com.crm.miniCRM.model.persistence.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.time.LocalDate;
import java.util.Optional;


//https://attacomsian.com/blog/spring-boot-upload-parse-csv-file
@SpringBootApplication
public class MiniCrmApplication {

    private static final Logger log = LoggerFactory.getLogger(MiniCrmApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(MiniCrmApplication.class);
    }

    @Bean
    public CommandLineRunner demoMiniCRM(PersonRepository personRepository,
                                         AddressRepository addressRepository,
                                         PersonAddressRepository personAddressRepository,
                                         CommunityRepository communityRepository,
                                         MemberRepository memberRepository,
                                         EventRepository eventRepository
    ) {
        return (args) -> {

//Person
            extractPersons(personRepository);
//Address
            extractAddresses(addressRepository);
//Community
            extractCommunities(communityRepository);
//Event
            extractEvents(eventRepository, communityRepository);
//Member
            extractMembers(memberRepository);
//PersonAddress
            extractPersonAddresses(personAddressRepository);
        };
    }

    private static void extractPersonAddresses(PersonAddressRepository personAddressRepository) {
        personAddressRepository.save(new PersonAddress(new PersonAddressID(1L, 1L), "a@b.com", "12343456", "34567789", 'P'));
        personAddressRepository.save(new PersonAddress(new PersonAddressID(2L, 1L), "b@c.com", "23434567", "34567789", 'P'));
        personAddressRepository.save(new PersonAddress(new PersonAddressID(3L, 2L), "d@e.com", "34345678", "34567789", 'P'));
        personAddressRepository.save(new PersonAddress(new PersonAddressID(4L, 3L), "f@g.com", "43456789", "34567789", 'P'));

        // fetch all person addresses
        log.info("PersonAddress found with findAll():");
        log.info("-------------------------------");
        for (PersonAddress personAddress : personAddressRepository.findAll()) {
            log.info(personAddress.toString());
        }
        log.info("");

        // fetch person addresses from an individual person by ID
        log.info("Addresses found with findById(new PersonAddressID()):");
        log.info("--------------------------------");
        Optional<PersonAddress> personAddress = personAddressRepository.findById(new PersonAddressID(1L, 1L));
        log.info(personAddress.toString());
        log.info("");
    }

    private static void extractEvents(EventRepository eventRepository, CommunityRepository communityRepository) {
        eventRepository.save(new Event(communityRepository.findById(1L),"kerstkaartjes 2020", LocalDate.of(2020, 12, 16)));
        eventRepository.save(new Event(communityRepository.findById(2L),"BBQ 2020", LocalDate.of(2020, 7, 21)));
        eventRepository.save(new Event(communityRepository.findById(3L),"doopfeest 2021", LocalDate.of(2021, 5, 31)));

        // fetch all events
        log.info("Event found with findAll():");
        log.info("-------------------------------");
        for (Event event : eventRepository.findAll()) {
            log.info(event.toString());
        }
        log.info("");

        // fetch an individual event by ID
        Event event = eventRepository.findById(1L);
        log.info("Event found with findById(1L):");
        log.info("--------------------------------");
        log.info(event.toString());
        log.info("");

        // fetch events by description
        log.info("Event found with findByDescription('BBQ 2020'):");
        log.info("--------------------------------------------");
        eventRepository.findByDescription("BBQ 2020").forEach(bbq -> {
            log.info(bbq.toString());
        });
        log.info("");
    }

    private static void extractCommunities(CommunityRepository communityRepository) {
        communityRepository.save(new Community("Jaarlijkse kerstkaartjes"));
        communityRepository.save(new Community("Jaarlijkse BBQ"));
        communityRepository.save(new Community("eenmalig doopfeest"));

        // fetch all communities
        log.info("Community found with findAll():");
        log.info("-------------------------------");
        for (Community community : communityRepository.findAll()) {
            log.info(community.toString());
        }
        log.info("");

        // fetch an individual community by ID
        Community community = communityRepository.findById(1L);
        log.info("Community found with findById(1L):");
        log.info("--------------------------------");
        log.info(community.toString());
        log.info("");

        // fetch community by description
        log.info("Community found with findByDescription('BBQ'):");
        log.info("--------------------------------------------");
        communityRepository.findByDescription("Jaarlijkse BBQ").forEach(bbq -> {
            log.info(bbq.toString());
        });
        log.info("");
    }

    private static void extractAddresses(AddressRepository addressRepository) {
        addressRepository.save(new Address("Tessenstraat", "7", "2", "3000", "Leuven", "België", "PRIV"));
        addressRepository.save(new Address("Brusselsestraat", "73", "2", "3000", "Leuven", "België", "PROF"));
        addressRepository.save(new Address("Tiensestraat", "76A", "", "3000", "Leuven", "België", "PRIV"));

        // fetch all addresses
        log.info("Address found with findAll():");
        log.info("-------------------------------");
        for (Address address : addressRepository.findAll()) {
            log.info(address.toString());
        }
        log.info("");

        // fetch an individual address by ID
        Address address = addressRepository.findById(1L);
        log.info("Address found with findById(1L):");
        log.info("--------------------------------");
        log.info(address.toString());
        log.info("");

        // fetch addresses by street
        log.info("Address found with findByStreet('Brusselsestraat'):");
        log.info("--------------------------------------------");
        addressRepository.findByStreet("Brusselsestraat").forEach(street -> {
            log.info(street.toString());
        });
        log.info("");
    }

    private static void extractPersons(PersonRepository personRepository) {
        personRepository.save(new Person("Jack", "Bauer", "1963-6-29"));
        personRepository.save(new Person("Kim", "Bauer", "1963-6-24"));
        personRepository.save(new Person("David", "Palmer", "1963-9-29"));
        personRepository.save(new Person("Michelle", "Dessler", "1963-11-29"));

        // fetch all persons
        log.info("Persons found with findAll():");
        log.info("-------------------------------");
        for (Person person : personRepository.findAll()) {
            log.info(person.toString());
        }
        log.info("");

        // fetch an individual person by ID
        Person person = personRepository.findById(1L);
        log.info("Person found with findById(1L):");
        log.info("--------------------------------");
        log.info(person.toString());
        log.info("");

        // fetch persons by last name
        log.info("Person found with findByLastName('Bauer'):");
        log.info("--------------------------------------------");
        personRepository.findByLastName("Bauer").forEach(bauer -> {
            log.info(bauer.toString());
        });
        log.info("");
    }

    //Members
    private void extractMembers(MemberRepository memberRepository) {
        memberRepository.save(new Member(new MemberID(1L, 1L), LocalDate.of(2003, 02, 01)));
        memberRepository.save(new Member(new MemberID(2L, 1L), LocalDate.of(2003, 02, 01)));
        memberRepository.save(new Member(new MemberID(3L, 1L), LocalDate.of(2003, 02, 01)));
        memberRepository.save(new Member(new MemberID(4L, 1L), LocalDate.of(1999, 8, 01)));

        memberRepository.save(new Member(new MemberID(1L, 2L), LocalDate.of(2013, 02, 01)));
        memberRepository.save(new Member(new MemberID(3L, 2L), LocalDate.of(2000, 02, 01)));
        memberRepository.save(new Member(new MemberID(4L, 2L), LocalDate.of(1999, 8, 01)));

        memberRepository.save(new Member(new MemberID(2L, 3L), LocalDate.of(2012, 02, 01)));
        memberRepository.save(new Member(new MemberID(3L, 3L), LocalDate.of(2005, 03, 01)));
        memberRepository.save(new Member(new MemberID(4L, 3L), LocalDate.of(1990, 8, 01)));

        // fetch all member
        log.info("Member found with findAll():");
        log.info("-------------------------------");
        for (Member member : memberRepository.findAll()) {
            log.info(member.toString());
        }
        log.info("");

        // fetch an individual member by ID
        Optional<Member> member = memberRepository.findById(new MemberID(3L, 1L));
        log.info("Member found with findById(new MemberID()):");
        log.info("--------------------------------");
        log.info(member.toString());
        log.info("");
    }


}