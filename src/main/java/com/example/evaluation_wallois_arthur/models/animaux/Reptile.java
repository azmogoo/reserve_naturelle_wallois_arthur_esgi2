package com.example.evaluation_wallois_arthur.models.animaux;

import com.example.evaluation_wallois_arthur.models.Animal;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public abstract class Reptile extends Animal {
    @JsonCreator
    public Reptile(
        @JsonProperty("nom") String nom,
        @JsonProperty("age") int age,
        @JsonProperty("poids") double poids,
        @JsonProperty("taille") double taille
    ) {
        super(nom, age, poids, taille);
    }

    public void pondreOeuf() {
        System.out.println(nom + " pond un œuf");
    }
} 