package com.crm.miniCRM.controller;

import com.crm.miniCRM.dto.EventDto;
import com.crm.miniCRM.model.Community;
import com.crm.miniCRM.model.Event;
import com.crm.miniCRM.model.persistence.CommunityRepository;
import com.crm.miniCRM.model.persistence.EventRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
        events.forEach(e -> { if(e.getActive()) {eventDtos.add(convertToDto(e));}});
        model.addAttribute("events", eventDtos);
        return "events";
    }

    @GetMapping("/new")
    public String newEvent(Model model) {
        List<Community> communityList = getActiveCommunities();
        model.addAttribute("communityList", communityList);
        model.addAttribute("event", new EventDto());
        return "new-event";
    }

    @PostMapping
    public String addEvent(EventDto event) {
        eventService.save(convertToEntity(event));

        return "redirect:/events";
    }

    @GetMapping("/edit/{id}")
    public String editEvent(Model model, @PathVariable Long id) {
        List<Community> communityList = getActiveCommunities();
        model.addAttribute("communityList", communityList);
        Optional<Event> optionalEvent = eventService.findById(id);
        if (optionalEvent.isPresent()){
            Event event = optionalEvent.get();
            model.addAttribute("event",convertToDto(event));
        }
        return "edit-event";
    }

    @GetMapping("/delete/{id}")
    public String deleteEvent(Model model, @PathVariable Long id) {
        Optional<Event> optionalEvent = eventService.findById(id);
        if (optionalEvent.isPresent()){
            Event event = optionalEvent.get();
            model.addAttribute("event",convertToDto(event));
        }
        return "delete-event";
    }

    @PostMapping("/delete")
    public String deleteEvent(EventDto event) {
        Event deletableEvent = convertToEntity(event);
        deletableEvent.setActive(false);
        eventService.save(deletableEvent);

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

    protected List<Community> getActiveCommunities() {
        List<Community> communityList = (List<Community>) communityService.findAll();
        List<Community> filteredCommunityList = new ArrayList<>();
        communityList.forEach(c -> { if(c.getActive()) {filteredCommunityList.add(c);}});
        return filteredCommunityList;
    }
}
