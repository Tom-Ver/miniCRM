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

import java.util.ArrayList;
import java.util.List;

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
    public String getMembers(Model model) { //should display names instead of id's, todo
        Iterable<Member> members = memberService.findAll();
        List<MemberDto> memberDtos = new ArrayList<>();
        members.forEach(m-> memberDtos.add(convertToDto(m)));
        model.addAttribute("members", memberDtos);
        return "members";
    }

    @GetMapping (value = "/by-community/{id}")
    public String getMembersByIdCommunity_ID(Model model, @PathVariable Long id) {
        Iterable<Member> members = memberService.findAllByIdCommunity_ID(id);
        List<MemberDto> memberDtos = new ArrayList<>();
        members.forEach(m-> memberDtos.add(convertToDto(m)));
        model.addAttribute("members", memberDtos);
        return "members-by-community";
    }

    @GetMapping("/new")
    public String newMember(Model model) {
        model.addAttribute("member", new MemberDto());
        List<Person> personList = (List<Person>) personService.findAll();
        model.addAttribute("personList", personList);
        List<Community> communityList = (List<Community>) communityService.findAll();
        model.addAttribute("communityList", communityList);
        return "new-member";
    }

    @GetMapping("/new-by-community/{id}")
    public String newMember(Model model, @PathVariable Long id) {
        MemberDto newMember = new MemberDto();
        newMember.setCommunity_ID(id);
        model.addAttribute("member", newMember);
        List<Person> personList = (List<Person>) personService.findAll();
        model.addAttribute("personList", personList);
        return "new-member-by-community";
    }

    @PostMapping
    public String addMember(MemberDto member) {
        memberService.save(convertToEntity(member));

        return "redirect:/members";
    }

    @PostMapping("/{id}")
    public String addMemberToCommunity(MemberDto member) {  //not working, todo
        memberService.save(convertToEntity(member));

        return "redirect:/members-by-community";
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

}
