package com.example.tractive.dto;

import com.example.tractive.entity.Cat;
import com.example.tractive.entity.Pet;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PetCatRequest {

    private Pet pet;
    private Cat cat;

}
