package com.example.tractive.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;
import com.example.tractive.dao.CatRepository;
import com.example.tractive.dao.PetRepository;
import com.example.tractive.entity.Cat;
import com.example.tractive.entity.Pet;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@SpringBootTest
public class PetServiceTest {
    @MockBean
    private PetRepository petRepository;
    @MockBean
    private CatRepository catRepository;
    @Autowired
    private PetService petService;

    @Test
    public void testSavePet() {

        // Create a sample Pet and Cat objects
        Pet pet = new Pet();
        pet.setPetType(Pet.PetType.DOG);
        pet.setTrackerType(Pet.TrackerType.SMALL);
        pet.setOwnerId(123);
        pet.setInZone(true);

        Cat cat = new Cat();
        cat.setLostTracker(false);

        // Mock the behavior of petRepository.save method to return the saved pet
        when(petRepository.save(any(Pet.class))).thenReturn(pet);

        // Call the method under test
        Pet savedPet = petService.savePet(pet, cat);

        // Verify that petRepository.save method was called with the correct Pet object
        ArgumentCaptor<Pet> petCaptor = ArgumentCaptor.forClass(Pet.class);
        verify(petRepository).save(petCaptor.capture());
        Pet capturedPet = petCaptor.getValue();
        assertEquals(pet, capturedPet);

        // Verify that catRepository.save method was called only if pet type is CAT
        if (pet.getPetType() == Pet.PetType.CAT) {
            ArgumentCaptor<Cat> catCaptor = ArgumentCaptor.forClass(Cat.class);
            verify(catRepository).save(catCaptor.capture());
            Cat capturedCat = catCaptor.getValue();

            // Assert that the associations are correctly set
            assertEquals(savedPet, capturedCat.getPet());
            assertEquals(cat, capturedCat);
        } else {
            // Ensure that catRepository.save method was not called
            verifyNoInteractions(catRepository);
        }
        // Verify that the method returns the saved pet
        assertEquals(pet, savedPet);
    }

    @Test
    public void testGetAllPets() {
        // Create a sample list of pets
        Pet pet1 = new Pet();
        pet1.setPetType(Pet.PetType.CAT);
        pet1.setTrackerType(Pet.TrackerType.SMALL);
        pet1.setOwnerId(1);
        pet1.setInZone(true);

        // Create a Cat object
        Cat cat = new Cat();
        cat.setPet(pet1);
        cat.setLostTracker(true); // Set other properties as needed

        // Set the Cat object back to the Pet object
        pet1.setCat(cat);

        Pet pet2 = new Pet(2,Pet.PetType.DOG, Pet.TrackerType.MEDIUM, 2, true,null);

        // Mock service behavior with the list of Pet entities
        List<Pet> mockPets = Arrays.asList(pet1,pet2);

        // Mock the behavior of petRepository.findAllPetsWithCat method to return the sample list of pets
        when(petRepository.findAllPetsWithCat()).thenReturn(mockPets);

        // Call the method under test
        List<Pet> result = petService.getAllPets();

        // Verify that petRepository.findAllPetsWithCat method was called
        verify(petRepository).findAllPetsWithCat();

        // Verify that the returned list of pets matches the mockPets list
        assertEquals(mockPets, result);
    }

    @Test
    public void testGetPetsOutsideZoneGroupedByTypeAndTracker() {
        // Create a sample list of Object arrays representing the query result
        List<Object[]> mockResultList = Arrays.asList(
                new Object[]{Pet.PetType.DOG, Pet.TrackerType.BIG, 5L},
                new Object[]{Pet.PetType.DOG, Pet.TrackerType.MEDIUM, 3L},
                new Object[]{Pet.PetType.CAT, Pet.TrackerType.SMALL, 2L}
        );

        // Mock the behavior of petRepository.countByPetTypeAndTrackerTypeAndInZoneFalse method to return the sample result list
        when(petRepository.countByPetTypeAndTrackerTypeAndInZoneFalse()).thenReturn(mockResultList);

        // Call the method under test
        Map<Pet.PetType, Map<Pet.TrackerType, Long>> result = petService.getPetsOutsideZoneGroupedByTypeAndTracker();

        // Verify that petRepository.countByPetTypeAndTrackerTypeAndInZoneFalse method was called
        verify(petRepository).countByPetTypeAndTrackerTypeAndInZoneFalse();

        // Verify the structure and content of the returned map
        assertEquals(2, result.size()); // Expecting 2 pet types (DOG and CAT)

        // Verify the content for each pet type
        assertTrue(result.containsKey(Pet.PetType.DOG));
        assertTrue(result.containsKey(Pet.PetType.CAT));

        // Verify the content for each tracker type within each pet type
        Map<Pet.TrackerType, Long> dogMap = result.get(Pet.PetType.DOG);
        assertEquals(2, dogMap.size()); // Expecting 2 tracker types for DOG
        assertEquals(5L, dogMap.get(Pet.TrackerType.BIG));
        assertEquals(3L, dogMap.get(Pet.TrackerType.MEDIUM));

        Map<Pet.TrackerType, Long> catMap = result.get(Pet.PetType.CAT);
        assertEquals(1, catMap.size()); // Expecting 1 tracker type for CAT
        assertEquals(2L, catMap.get(Pet.TrackerType.SMALL));
    }
}

