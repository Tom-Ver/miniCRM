package com.crm.miniCRM.controller;


import com.crm.miniCRM.dto.MemberDto;
import com.crm.miniCRM.model.Member;
import com.crm.miniCRM.model.persistence.MemberID;
import com.crm.miniCRM.model.persistence.MemberRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping(value = "/members")
public class MemberController {
    private MemberRepository memberService;

    public MemberController(MemberRepository memberService) {
        this.memberService = memberService;
    }

    @GetMapping
    public String getMembers(Model model) {
        Iterable<Member> members = memberService.findAll();
        List<MemberDto> memberDtos = new ArrayList<>();
        members.forEach(m-> memberDtos.add(convertToDto(m)));
        model.addAttribute("members", memberDtos);
        return "members";
    }

    @GetMapping("/new")
    public String newMember(Model model) {
        model.addAttribute("member", new MemberDto());
        return "new-member";
    }

    @PostMapping
    public String addMember(MemberDto member) {
        memberService.save(convertToEntity(member));

        return "redirect:/members";
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
