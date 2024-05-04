package com.example.tractive.integration;

import com.example.tractive.dto.PetCatRequest;
import com.example.tractive.entity.Cat;
import com.example.tractive.entity.Pet;
import com.example.tractive.service.PetService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class PetControllerIntegrationTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private PetService petService;

	@Test
	public void testSavePetWithCat() throws Exception {
		// Mock request data
		Pet pet = new Pet();
		pet.setPetType(Pet.PetType.DOG);
		pet.setTrackerType(Pet.TrackerType.SMALL);
		pet.setOwnerId(123);
		pet.setInZone(true);

		Cat cat = new Cat();
		cat.setLostTracker(false);

		PetCatRequest request = new PetCatRequest();
		request.setPet(pet);
		request.setCat(cat);

		// Mock service behavior
		when(petService.savePet(any(Pet.class), any(Cat.class))).thenReturn(pet);

		// Perform POST request and verify response
		mockMvc.perform(MockMvcRequestBuilders.post("/pets/save")
						.contentType(MediaType.APPLICATION_JSON)
						.content(asJsonString(request)))
				.andExpect(MockMvcResultMatchers.status().isOk());

		// Verify that the petService.savePet method was called
		Mockito.verify(petService, Mockito.times(1)).savePet(any(Pet.class), any(Cat.class));
	}

	@Test
	public void testGetAllPets() throws Exception {

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

		// Mocking the petService.getAllPets() method to return the list of PetEntity
		when(petService.getAllPets()).thenReturn(mockPets);

		// Perform GET request and verify response
		mockMvc.perform(MockMvcRequestBuilders.get("/pets/all")
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(result -> {
					String content = result.getResponse().getContentAsString();
					List<Pet> responsePets = new ObjectMapper().readValue(content, new TypeReference<List<Pet>>() {});
					// Verify that the response contains the expected number of pets
					assert responsePets.size() == mockPets.size();
				});
		// Verify that the petService.getAllPets method was called
		Mockito.verify(petService, Mockito.times(1)).getAllPets();
	}

	@Test
	public void testGetPetsOutsideZoneGroupedByTypeAndTracker() throws Exception {
		// Mock service behavior
		Map<Pet.PetType, Map<Pet.TrackerType, Long>> mockResult = new HashMap<>();
		// Add some mock data to the result
		// For example:
		mockResult.put(Pet.PetType.DOG, new HashMap<>());
		mockResult.get(Pet.PetType.DOG).put(Pet.TrackerType.BIG, 5L);
		mockResult.get(Pet.PetType.DOG).put(Pet.TrackerType.MEDIUM, 3L);

		// Mocking the petService.getPetsOutsideZoneGroupedByTypeAndTracker() method to return the mock result
		when(petService.getPetsOutsideZoneGroupedByTypeAndTracker()).thenReturn(mockResult);

		// Perform GET request and verify response
		mockMvc.perform(MockMvcRequestBuilders.get("/pets/outside-zone")
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(result -> {
					String content = result.getResponse().getContentAsString();
					Map<Pet.PetType, Map<Pet.TrackerType, Long>> responseResult = new ObjectMapper().readValue(content, new TypeReference<Map<Pet.PetType, Map<Pet.TrackerType, Long>>>() {});

					// Verify that the response matches the expected result
					assert responseResult.equals(mockResult);

					assertFalse(responseResult.isEmpty(), "Response should not be empty");

					// Verify that the response contains data for all expected tracker types for each pet type
					for (Pet.PetType petType : responseResult.keySet()) {
						assertTrue(responseResult.get(petType).keySet().containsAll(responseResult.get(petType).keySet()), "Response should contain data for all expected tracker types for pet type: " + petType);
					}

					// Verify the count of pets for each pet type and tracker type
					for (Pet.PetType petType : responseResult.keySet()) {
						for (Pet.TrackerType trackerType : responseResult.get(petType).keySet()) {
							assertTrue(responseResult.get(petType).containsKey(trackerType), "Response should contain data for tracker type: " + trackerType + " for pet type: " + petType);
							assertEquals(responseResult.get(petType).get(trackerType), responseResult.get(petType).get(trackerType), "Incorrect count for tracker type: " + trackerType + " for pet type: " + petType);
						}
					}
				});

		// Verify that the petService.getPetsOutsideZoneGroupedByTypeAndTracker method was called
		Mockito.verify(petService, Mockito.times(1)).getPetsOutsideZoneGroupedByTypeAndTracker();
	}

	// Utility method to convert object to JSON string
	private String asJsonString(final Object obj) {
		try {
			return new ObjectMapper().writeValueAsString(obj);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}