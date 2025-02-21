package com.example.evaluation_wallois_arthur.models.habitats;

import com.example.evaluation_wallois_arthur.models.TailleHabitat;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Aquarium extends Habitat {
    private double profondeur;

    @JsonCreator
    public Aquarium(String nom, TailleHabitat taille) {
        super(nom, taille);
        this.profondeur = 5.0; // Profondeur par défaut
    }

    @JsonProperty("profondeur")
    public double getProfondeur() {
        return profondeur;
    }

    public void setProfondeur(double profondeur) {
        this.profondeur = profondeur;
    }

    @Override
    public String toString() {
        return "Aquarium - " + super.toString() + " (profondeur: " + profondeur + "m)";
    }
} 