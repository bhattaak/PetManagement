package com.example.tractive.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "cat")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Cat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cat_id")
    private Integer id; // Unique identifier for the cat

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pet_id")
    @JsonBackReference
    private Pet pet; // One-to-one relationship with Pet entity (associated pet details)

    @Column(name = "lost_tracker")
    private Boolean lostTracker; // Indicates whether the tracker is lost for the cat
}
