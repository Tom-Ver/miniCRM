package com.crm.miniCRM.controller;

import com.crm.miniCRM.dto.EventDto;
import com.crm.miniCRM.model.Community;
import com.crm.miniCRM.model.Event;
import com.crm.miniCRM.model.persistence.CommunityRepository;
import com.crm.miniCRM.model.persistence.EventRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping(value="/events")
public class EventController {

    private EventRepository eventService;
    private CommunityRepository communityService;

    public EventController(EventRepository eventService, CommunityRepository communityService) {
        this.eventService = eventService;
        this.communityService = communityService;
    }

    @GetMapping
    public String getEvents(Model model) {
        Iterable<Event> events = eventService.findAll();
        List<EventDto> eventDtos = new ArrayList<>();
        events.forEach(p-> eventDtos.add(convertToDto(p)));
        model.addAttribute("events", eventDtos);
        return "events";
    }

    @GetMapping("/new")
    public String newEvent(Model model) {
        List<Community> communityList = (List<Community>) communityService.findAll();
        model.addAttribute("communityList", communityList);
        model.addAttribute("event", new EventDto());
        return "new-event";
    }

    @PostMapping
    public String addEvent(EventDto event) {
        eventService.save(convertToEntity(event));

        return "redirect:/events";
    }

    protected EventDto convertToDto(Event entity) {
        EventDto dto = new EventDto(
                entity.getCommunity(),
                entity.getId(),
                entity.getDescription(),
                entity.getDate());
        return dto;
    }

    protected Event convertToEntity(EventDto dto) {
        Event event = new Event(
                dto.getCommunity(),
                dto.getDescription(),
                dto.getDate());
        if (!StringUtils.isEmpty(dto.getId())) {
            event.setId(dto.getId());
        }
        return event;
    }

}
