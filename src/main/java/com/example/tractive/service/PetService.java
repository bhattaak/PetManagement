package com.example.tractive.service;

import com.example.tractive.dao.PetRepository;
import com.example.tractive.dao.CatRepository;
import com.example.tractive.entity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
@Service
public class PetService {
    private final PetRepository petRepository;
    private final CatRepository catRepository;

    // Constructor injection of repositories
    @Autowired
    public PetService(PetRepository petRepository, CatRepository catRepository) {
        this.petRepository = petRepository;
        this.catRepository = catRepository;
    }

    // Save a pet entity
    @Transactional
    public Pet savePet(Pet pet, Cat cat) {

        // Save the pet entity
        Pet savedPet = petRepository.save(pet);

        // If the pet is a cat, set the association on both sides
        if(pet.getPetType().equals(Pet.PetType.CAT)){
            cat.setPet(savedPet);
            savedPet.setCat(cat);
        }
        return savedPet;
    }

    @Transactional(readOnly = true)
    public List<Pet> getAllPets() {

        // Fetch all pets from the repository
        List<Pet> pets = petRepository.findAllPetsWithCat();
        return pets;
    }

    @Transactional(readOnly = true)
    public Map<Pet.PetType, Map<Pet.TrackerType, Long>> getPetsOutsideZoneGroupedByTypeAndTracker() {

        // Fetch the result list from the repository
        List<Object[]> resultList = petRepository.countByPetTypeAndTrackerTypeAndInZoneFalse();
        Map<Pet.PetType, Map<Pet.TrackerType, Long>> resultMap = new HashMap<>();

        // Process the result list and populate the result map
        for (Object[] result : resultList) {
            Pet.PetType petType = (Pet.PetType) result[0];
            Pet.TrackerType trackerType = (Pet.TrackerType) result[1];
            Long count = (Long) result[2];

            resultMap.computeIfAbsent(petType, k -> new HashMap<>()).put(trackerType, count);
        }
        return resultMap;
    }

}


