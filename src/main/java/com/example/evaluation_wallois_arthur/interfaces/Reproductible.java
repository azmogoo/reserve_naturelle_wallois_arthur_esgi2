package com.example.evaluation_wallois_arthur.interfaces;

import com.example.evaluation_wallois_arthur.models.Animal;

public interface Reproductible {
    boolean seReproduire(Animal partenaire);
    boolean estFertile();
    boolean peutSeReproduireAvec(Animal partenaire);
    int getAgeMinimumReproduction();
    int getDelaiEntreReproductions();
    void incrementerJoursDepuisReproduction();
} 