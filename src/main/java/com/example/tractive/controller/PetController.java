package com.example.tractive.controller;

import com.example.tractive.dto.PetCatRequest;
import com.example.tractive.entity.Pet;
import com.example.tractive.service.PetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/pets")
public class PetController {
    private final PetService petService;

    @Autowired
    public PetController(PetService petService) {
        this.petService = petService;
    }

    // Save pet with associated cat
    @PostMapping("/save")
    public ResponseEntity<Pet> savePetWithCat(@RequestBody PetCatRequest request) {

        // Call service to save pet
        Pet savedPet = petService.savePet(request.getPet(), request.getCat());
        return ResponseEntity.ok(savedPet);
    }

    // Get all pets
    @GetMapping("/all")
    public ResponseEntity<List<Pet>> getAllPets() {

        // Call service to fetch all pets
        List<Pet> allPets = petService.getAllPets();
        return ResponseEntity.ok(allPets);
    }

    // Get pets outside zone grouped by type and tracker
    @GetMapping("/outside-zone")
    public ResponseEntity<Map<Pet.PetType, Map<Pet.TrackerType, Long>>> getPetsOutsideZoneGroupedByTypeAndTracker() {

        // Call service to fetch pets outside zone grouped by type and tracker
        Map<Pet.PetType, Map<Pet.TrackerType, Long>> result = petService.getPetsOutsideZoneGroupedByTypeAndTracker();
        return ResponseEntity.ok(result);
    }
}
