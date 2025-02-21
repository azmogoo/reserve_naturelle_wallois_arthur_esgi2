package com.example.evaluation_wallois_arthur.models.animaux;

import com.example.evaluation_wallois_arthur.models.Animal;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Lion extends Mammifere {
    @JsonCreator
    public Lion(
        @JsonProperty("nom") String nom,
        @JsonProperty("age") int age,
        @JsonProperty("poids") double poids,
        @JsonProperty("taille") double taille
    ) {
        super(nom, age, poids, taille);
    }

    public void rugir() {
        System.out.println(nom + " rugit puissamment !");
    }

    @Override
    public void emettreSon() {
        rugir();
    }

    @Override
    public void voler() {
        System.out.println(nom + " ne peut pas voler");
    }

    @Override
    public void nager() {
        System.out.println(nom + " nage avec difficulté");
    }

    @Override
    public void marcher() {
        System.out.println(nom + " marche majestueusement");
    }
} 