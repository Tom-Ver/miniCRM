package com.crm.miniCRM.controller;

import com.crm.miniCRM.dto.CommunityDto;
import com.crm.miniCRM.dto.EventDto;
import com.crm.miniCRM.model.Community;
import com.crm.miniCRM.model.Event;
import com.crm.miniCRM.model.persistence.CommunityRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Controller
@RequestMapping(value = "/communities")
public class CommunityController {

    private CommunityRepository communityService;

    public CommunityController(CommunityRepository communityService) {
        this.communityService = communityService;
    }

    @GetMapping
    public String getCommunities(Model model) {
        Iterable<Community> communities = communityService.findAll();
        List<CommunityDto> communityDtos = new ArrayList<>();
        communities.forEach(c -> { if(c.getActive()) {communityDtos.add(convertToDto(c));} });
        model.addAttribute("communities", communityDtos);
        return "communities";
    }

    @GetMapping("/new")
    public String newCommunity(Model model) {
        model.addAttribute("community", new CommunityDto());
        return "new-community";
    }

    @PostMapping
    public String addCommunity(CommunityDto community) {
        communityService.save(convertToEntity(community));

        return "redirect:/communities";
    }

    @GetMapping("/edit/{id}")
    public String editCommunity(Model model, @PathVariable Long id) {
        Optional<Community> optionalCommunity = communityService.findById(id);
        if (optionalCommunity.isPresent()){
            Community community = optionalCommunity.get();
            model.addAttribute("community",convertToDto(community));
        }
        return "edit-community";
    }

    @GetMapping("/delete/{id}")
    public String deleteCommunity(Model model, @PathVariable Long id) {
        Optional<Community> optionalCommunity = communityService.findById(id);
        if (optionalCommunity.isPresent()){
            Community community = optionalCommunity.get();
            model.addAttribute("community",convertToDto(community));
        }
        return "delete-community";
    }

    @PostMapping("/delete")
    public String deleteCommunity(CommunityDto community) {
        Community deletableCommunity = convertToEntity(community);
        deletableCommunity.setActive(false);
        communityService.save(deletableCommunity);

        return "redirect:/communities";
    }

    protected CommunityDto convertToDto(Community entity) {
        CommunityDto dto = new CommunityDto(entity.getID(), entity.getDescription());
         return dto;
    }

    protected Community convertToEntity(CommunityDto dto) {
        //29-06-1963

        Community community = new Community(dto.getDescription());
        if (!StringUtils.isEmpty(dto.getId())) {
            community.setID(dto.getId());
        }
        return community;
    }



}
