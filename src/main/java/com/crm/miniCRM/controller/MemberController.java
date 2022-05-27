package com.crm.miniCRM.controller;


import com.crm.miniCRM.dto.MemberDto;
import com.crm.miniCRM.model.Community;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        members.forEach(m-> memberDtos.add(convertToDto(m)));
        model.addAttribute("members", memberDtos);
        return "members";
    }

    @GetMapping (value = "/members-by-community/{id}")
    public String getMembersByIdCommunity_ID(Model model, @PathVariable Long id) {
        Map<Long,String> personMap = getPersonMap();
        model.addAttribute("personMap", personMap);

        Iterable<Member> members = memberService.findAllByIdCommunity_ID(id);
        List<MemberDto> memberDtos = new ArrayList<>();
        members.forEach(m-> memberDtos.add(convertToDto(m)));
        model.addAttribute("members", memberDtos);
        return "members-by-community";
    }

    @GetMapping("/new")
    public String newMember(Model model) {
        List<Person> personList = (List<Person>) personService.findAll();
        model.addAttribute("personList", personList);
        List<Community> communityList = (List<Community>) communityService.findAll();
        model.addAttribute("communityList", communityList);

        MemberDto newMember = new MemberDto();
        newMember.setSince(LocalDate.now());
        model.addAttribute("member", newMember);
        return "new-member";
    }

    @GetMapping("/new-by-community/{id}")
    public String newMemberByCommunity_ID(Model model, @PathVariable Long id) {
        List<Person> personList = (List<Person>) personService.findAll();
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
}
