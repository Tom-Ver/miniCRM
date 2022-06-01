package com.crm.miniCRM.controller;


import com.crm.miniCRM.dto.EventDto;
import com.crm.miniCRM.dto.MemberDto;
import com.crm.miniCRM.model.Community;
import com.crm.miniCRM.model.Event;
import com.crm.miniCRM.model.Member;
import com.crm.miniCRM.model.Person;
import com.crm.miniCRM.model.persistence.CommunityRepository;
import com.crm.miniCRM.model.persistence.MemberID;
import com.crm.miniCRM.model.persistence.MemberRepository;
import com.crm.miniCRM.model.persistence.PersonRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDate;
import java.util.*;

@Controller
@RequestMapping(value = "/members")
public class MemberController {
    private MemberRepository memberService;
    private PersonRepository personService;
    private CommunityRepository communityService;

    public MemberController(MemberRepository memberService, PersonRepository personService, CommunityRepository communityService) {
        this.memberService = memberService;
        this.personService = personService;
        this.communityService = communityService;
    }

    @GetMapping
    public String getMembers(Model model) {
        Map<Long,String> personMap = getPersonMap();
        model.addAttribute("personMap", personMap);
        Map<Long,String> communityMap = getCommunityMap();
        model.addAttribute("communityMap", communityMap);

        Iterable<Member> members = memberService.findAll();
        List<MemberDto> memberDtos = new ArrayList<>();
        members.forEach(m-> { if(m.getActive()) {memberDtos.add(convertToDto(m));} });
        model.addAttribute("members", memberDtos);
        return "members";
    }

    @GetMapping (value = "/members-by-community/{id}")
    public String getMembersByIdCommunity(Model model, @PathVariable Long id) {
        Map<Long,String> personMap = getPersonMap();
        model.addAttribute("personMap", personMap);
        Optional<Community> optionalCommunity = communityService.findById(id);
        if (optionalCommunity.isPresent()) {
            String description = optionalCommunity.get().getDescription();
            model.addAttribute("description", description);
        }
        Iterable<Member> members = memberService.findAllByIdCommunity_ID(id);
        List<MemberDto> memberDtos = new ArrayList<>();
        members.forEach(m-> { if(m.getActive()) {memberDtos.add(convertToDto(m));} });
        model.addAttribute("members", memberDtos);
        return "members-by-community";
    }

    @GetMapping("/new")
    public String newMember(Model model) {
        List<Person> personList = getActivePersons();
        model.addAttribute("personList", personList);
        List<Community> communityList = getActiveCommunities();
        model.addAttribute("communityList", communityList);

        MemberDto newMember = new MemberDto();
        newMember.setSince(LocalDate.now());
        model.addAttribute("member", newMember);
        return "new-member";
    }

    @GetMapping("/new-by-community/{id}")
    public String newMemberByCommunity(Model model, @PathVariable Long id) {
        List<Person> personList = getActivePersons();
        model.addAttribute("personList", personList);

        MemberDto newMember = new MemberDto();
        newMember.setCommunity_ID(id);
        newMember.setSince(LocalDate.now());
        model.addAttribute("member", newMember);
        return "new-member-by-community";
    }

    @PostMapping
    public String addMember(MemberDto member) {
        memberService.save(convertToEntity(member));
        return "redirect:/members";
    }

    @PostMapping("/{id}")
    public String addMemberToCommunity(MemberDto member) {
        memberService.save(convertToEntity(member));

        return "redirect:/members/members-by-community/{id}";
    }

    @GetMapping("/edit/{person_ID}/{community_ID}")
    public String editMember(Model model, @PathVariable Long person_ID, @PathVariable Long community_ID) {
        Optional<Member> optionalMember = memberService.findById(new MemberID(person_ID, community_ID));
        if (optionalMember.isPresent()){
            Member member = optionalMember.get();
            model.addAttribute("member",convertToDto(member));
            model.addAttribute("name", getPersonName(member));
            model.addAttribute("description", getCommunityDescription(member));
        }
        return "edit-member";
    }

    @GetMapping("/edit-by-community/{person_ID}/{community_ID}")
    public String editMemberByCommunity(Model model, @PathVariable Long person_ID, @PathVariable Long community_ID) {
        Optional<Member> optionalMember = memberService.findById(new MemberID(person_ID, community_ID));
        if (optionalMember.isPresent()){
            Member member = optionalMember.get();
            model.addAttribute("member",convertToDto(member));
            model.addAttribute("name", getPersonName(member));
        }
        return "edit-member-by-community";
    }

    @GetMapping("/delete/{person_ID}/{community_ID}")
    public String deleteMember(Model model, @PathVariable Long person_ID, @PathVariable Long community_ID) {
        Optional<Member> optionalMember = memberService.findById(new MemberID(person_ID, community_ID));
        if (optionalMember.isPresent()){
            Member member = optionalMember.get();
            model.addAttribute("member",convertToDto(member));
            model.addAttribute("name", getPersonName(member));
            model.addAttribute("description", getCommunityDescription(member));
        }
        return "delete-member";
    }

    @PostMapping("/delete")
    public String deleteMember(MemberDto member) {
        Member deletableMember = convertToEntity(member);
        deletableMember.setActive(false);
        memberService.save(deletableMember);

        return "redirect:/members";
    }

    @GetMapping("/delete-by-community/{person_ID}/{community_ID}")
    public String deleteMemberByCommunity(Model model, @PathVariable Long person_ID, @PathVariable Long community_ID) {
        Optional<Member> optionalMember = memberService.findById(new MemberID(person_ID, community_ID));
        if (optionalMember.isPresent()){
            Member member = optionalMember.get();
            model.addAttribute("member",convertToDto(member));
            model.addAttribute("name", getPersonName(member));
        }
        return "delete-member-by-community";
    }

    @PostMapping("/delete-by-community/{id}")
    public String deleteMemberByCommunity(MemberDto member) {
        Member deletableMember = convertToEntity(member);
        deletableMember.setActive(false);
        memberService.save(deletableMember);

        return "redirect:/members/members-by-community/{id}";
    }

    @GetMapping("/delete-all-members/{id}")
    public String deleteAllMembersByCommunity(Model model, @PathVariable Long id) {
        Optional<Community> optionalCommunity = communityService.findById(id);
        if (optionalCommunity.isPresent()) {
            String description = optionalCommunity.get().getDescription();
            model.addAttribute("description", description);
        }
        return "delete-all-members";
    }

    @PostMapping("/delete-all-members/{id}")
    public String deleteAllMembersByCommunity(@PathVariable Long id) {
        Iterable<Member> deletableMembers = memberService.findAllByIdCommunity_ID(id);
        deletableMembers.forEach(m -> m.setActive(false));
        memberService.saveAll(deletableMembers);
        return "redirect:/members/members-by-community/{id}";
    }

    protected MemberDto convertToDto(Member entity) {
        MemberDto dto = new MemberDto(
                entity.getId(),
                entity.getSince(),
                entity.getUntil());
        return dto;
    }

    protected Member convertToEntity(MemberDto dto) {
        Member member = new Member(
                new MemberID(dto.getPerson_ID(), dto.getCommunity_ID()),
                dto.getSince(),
                dto.getUntil());
        return member;
    }

    protected Map<Long, String> getPersonMap(){
        Map<Long,String> personMap = new HashMap<>();
        Iterable<Person> persons = personService.findAll();
        persons.forEach( p -> personMap.put(p.getId(), p.getFirstName() + " " + p.getLastName()));
        return personMap;
    }

    protected Map<Long, String> getCommunityMap() {
        Map<Long, String> communityMap = new HashMap<>();
        Iterable<Community> communities = communityService.findAll();
        communities.forEach(c -> communityMap.put(c.getID(), c.getDescription()));
        return communityMap;
    }

    protected String getPersonName(Member member) {
        String name = null;
        Optional<Person> optionalPerson = personService.findById(member.getId().getPerson_ID());
        if (optionalPerson.isPresent()) {
            name = optionalPerson.get().getFirstName() + " " + optionalPerson.get().getLastName();
        }
        return name;
    }

    protected String getCommunityDescription(Member member) {
        String description = null;
        Optional<Community> optionalCommunity = communityService.findById(member.getId().getCommunity_ID());
        if (optionalCommunity.isPresent()){
            description = optionalCommunity.get().getDescription();
        }
        return description;
    }

    protected List<Person> getActivePersons() {
        List<Person> personList = (List<Person>) personService.findAll();
        List<Person> filteredPersonList = new ArrayList<>();
        personList.forEach(p -> { if(p.getActive()) {filteredPersonList.add(p);}});
        return filteredPersonList;
    }

    protected List<Community> getActiveCommunities() {
        List<Community> communityList = (List<Community>) communityService.findAll();
        List<Community> filteredCommunityList = new ArrayList<>();
        communityList.forEach(c -> { if(c.getActive()) {filteredCommunityList.add(c);}});
        return filteredCommunityList;
    }
}
