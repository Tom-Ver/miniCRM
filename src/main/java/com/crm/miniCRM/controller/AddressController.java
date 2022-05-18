package com.crm.miniCRM.controller;

import com.crm.miniCRM.dto.AddressDto;
import com.crm.miniCRM.dto.CommunityDto;
import com.crm.miniCRM.model.Address;
import com.crm.miniCRM.model.Community;
import com.crm.miniCRM.model.persistence.AddressRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping(value="/addresses")
public class AddressController {

    private AddressRepository addressService;

    public AddressController(AddressRepository addressService) {
        this.addressService = addressService;
    }

    @GetMapping
    public String getAddresses(Model model) {
        Iterable<Address> addresses = addressService.findAll();
        List<AddressDto> addressDtos = new ArrayList<>();
        addresses.forEach(p-> addressDtos.add(convertToDto(p)));
        model.addAttribute("addresses", addressDtos);
        return "addresses";
    }

    @GetMapping("/new")
    public String newAddress(Model model) {
        model.addAttribute("address", new AddressDto());
        return "new-address";
    }

    @PostMapping
    public String addAddress(AddressDto address) {
        addressService.save(convertToEntity(address));

        return "redirect:/addresses";
    }

    protected AddressDto convertToDto(Address entity) {
        AddressDto dto = new AddressDto(
                entity.getId(),
                entity.getStreet(),
                entity.getNumber(),
                entity.getBox(),
                entity.getZip(),
                entity.getCity(),
                entity.getCountry(),
                entity.getType());
        return dto;
    }

    protected Address convertToEntity(AddressDto dto) {
        Address address = new Address(dto.getStreet(),
                dto.getNumber(),
                dto.getBox(),
                dto.getZip(),
                dto.getCity(),
                dto.getCountry(),
                dto.getType());
        if (!StringUtils.isEmpty(dto.getId())) {
            address.setId(dto.getId());
        }
        return address;
    }

}
