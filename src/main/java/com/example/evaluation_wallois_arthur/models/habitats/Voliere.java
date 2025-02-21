package com.example.evaluation_wallois_arthur.models.habitats;

import com.example.evaluation_wallois_arthur.models.Animal;
import com.example.evaluation_wallois_arthur.models.TailleHabitat;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Voliere extends Habitat {
    private double hauteur;

    public Voliere(String nom, TailleHabitat taille) {
        super(nom, taille);
        this.hauteur = 10.0; // Hauteur par défaut
    }

    public double getHauteur() {
        return hauteur;
    }

    public void setHauteur(double hauteur) {
        this.hauteur = hauteur;
    }

    @Override
    public String toString() {
        return "Volière - " + super.toString() + " (hauteur: " + hauteur + "m)";
    }
} 