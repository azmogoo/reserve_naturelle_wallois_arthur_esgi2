package com.example.evaluation_wallois_arthur.models.animaux;

import com.example.evaluation_wallois_arthur.models.Animal;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Aigle extends Oiseau {
    @JsonCreator
    public Aigle(
        @JsonProperty("nom") String nom,
        @JsonProperty("age") int age,
        @JsonProperty("poids") double poids,
        @JsonProperty("taille") double taille
    ) {
        super(nom, age, poids, taille);
    }

    public void crier() {
        System.out.println(nom + " pousse un cri perçant !");
    }

    @Override
    public void emettreSon() {
        crier();
    }

    @Override
    public void voler() {
        System.out.println(nom + " plane majestueusement dans les airs");
    }

    @Override
    public void nager() {
        System.out.println(nom + " ne nage pas");
    }

    @Override
    public void marcher() {
        System.out.println(nom + " sautille sur le sol");
    }
} 