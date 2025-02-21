package com.example.evaluation_wallois_arthur.models.animaux;

import com.example.evaluation_wallois_arthur.models.Animal;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public abstract class Mammifere extends Animal {
    @JsonCreator
    public Mammifere(
        @JsonProperty("nom") String nom,
        @JsonProperty("age") int age,
        @JsonProperty("poids") double poids,
        @JsonProperty("taille") double taille
    ) {
        super(nom, age, poids, taille);
    }

    public void allaiter() {
        System.out.println(nom + " allaite ses petits");
    }

    public abstract void voler();
    public abstract void nager();
    public abstract void marcher();
} 