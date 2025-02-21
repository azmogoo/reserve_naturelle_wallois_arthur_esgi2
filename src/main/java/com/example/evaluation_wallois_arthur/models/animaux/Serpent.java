package com.example.evaluation_wallois_arthur.models.animaux;

import com.example.evaluation_wallois_arthur.models.Animal;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Serpent extends Reptile {
    @JsonCreator
    public Serpent(
        @JsonProperty("nom") String nom,
        @JsonProperty("age") int age,
        @JsonProperty("poids") double poids,
        @JsonProperty("taille") double taille
    ) {
        super(nom, age, poids, taille);
    }

    public void siffler() {
        System.out.println(nom + " siffle...");
    }

    @Override
    public void emettreSon() {
        siffler();
    }

    @Override
    public void voler() {
        System.out.println(nom + " ne peut pas voler");
    }

    @Override
    public void nager() {
        System.out.println(nom + " nage en ondulant");
    }

    @Override
    public void marcher() {
        System.out.println(nom + " rampe sur le sol");
    }
} 