package com.example.evaluation_wallois_arthur.models.animaux;

import com.example.evaluation_wallois_arthur.models.Animal;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public abstract class Oiseau extends Animal {
    @JsonCreator
    public Oiseau(
        @JsonProperty("nom") String nom,
        @JsonProperty("age") int age,
        @JsonProperty("poids") double poids,
        @JsonProperty("taille") double taille
    ) {
        super(nom, age, poids, taille);
    }

    @Override
    public void voler() {
        System.out.println(nom + " vole dans les airs");
    }

    @Override
    public abstract void nager();

    @Override
    public abstract void marcher();
} 