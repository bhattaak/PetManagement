package com.example.tractive.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "pet")
@Inheritance(strategy = InheritanceType.JOINED)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Pet {
    public  enum PetType {CAT, DOG}
    public  enum TrackerType {BIG, MEDIUM, SMALL}

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pet_id")
    private Integer id; // Unique identifier for the pet

    @Enumerated(EnumType.STRING)
    @Column(name = "pet_type")
    private PetType petType; // Type of the pet (CAT or DOG)

    @Enumerated(EnumType.STRING)
    @Column(name = "tracker_type")
    private TrackerType trackerType; // Type of the tracker associated with the pet

    @Column(name = "owner_id")
    private Integer ownerId; // ID of the owner of the pet

    @Column(name = "in_zone")
    private Boolean inZone; // Indicates whether the pet is in the zone (true) or outside the zone (false)

    @OneToOne(mappedBy = "pet", cascade = CascadeType.ALL)
    @JsonManagedReference // Include cat field in JSON response
    private Cat cat; // One-to-one relationship with Cat entity (associated cat details)


}

