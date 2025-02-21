package com.example.evaluation_wallois_arthur.models.habitats;

import com.example.evaluation_wallois_arthur.models.TailleHabitat;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Savane extends Habitat {
    @JsonCreator
    public Savane(
        @JsonProperty("nom") String nom,
        @JsonProperty("taille") TailleHabitat taille
    ) {
        super(nom, taille);
    }

    @Override
    public String toString() {
        return "Savane - " + super.toString();
    }
} 