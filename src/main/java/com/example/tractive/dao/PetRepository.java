package com.example.tractive.dao;

import com.example.tractive.entity.Pet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface PetRepository extends JpaRepository<Pet, Integer> {

    // Custom query to fetch all pets with associated cat entities
    @Query("SELECT p FROM Pet p LEFT JOIN FETCH p.cat")
    List<Pet> findAllPetsWithCat();


    // Custom query to count pets grouped by type and tracker outside the zone
    @Query("SELECT p.petType, p.trackerType, COUNT(p) " +
            "FROM Pet p " +
            "WHERE p.inZone = false " +
            "GROUP BY p.petType, p.trackerType")
    List<Object[]> countByPetTypeAndTrackerTypeAndInZoneFalse();
}

