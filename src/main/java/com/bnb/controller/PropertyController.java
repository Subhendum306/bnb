package com.bnb.controller;

import com.bnb.entity.Property;
import com.bnb.repository.PropertyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/property")
public class PropertyController {
    public PropertyController(PropertyRepository propertyRepository) {
        this.propertyRepository = propertyRepository;
    }

    @Autowired
    private PropertyRepository propertyRepository;

    @GetMapping("/propertyresult")
    public List<Property> searchProperty(@RequestParam("city") String name) {

        return propertyRepository.searchProperty(name);

    }
}
