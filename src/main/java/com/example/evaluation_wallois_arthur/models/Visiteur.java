package com.example.evaluation_wallois_arthur.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Visiteur {
    private String nom;
    private int age;
    private String billet;
    private boolean aAcheteBillet;
    private double satisfaction;
    private int nombreAnimauxObserves;

    private static final double PRIX_BILLET_ADULTE = 50.0;
    private static final double PRIX_BILLET_ENFANT = 25.0;

    @JsonCreator
    public Visiteur(
        @JsonProperty("nom") String nom,
        @JsonProperty("age") int age
    ) {
        this.nom = nom;
        this.age = age;
        this.aAcheteBillet = false;
        this.satisfaction = 100.0;
        this.nombreAnimauxObserves = 0;
    }

    public void observerAnimal(Animal animal) {
        if (aAcheteBillet) {
            System.out.println(nom + " observe " + animal.getNom());
            nombreAnimauxObserves++;
            // La satisfaction augmente si l'animal est en bonne santé
            if ("Bon".equals(animal.getEtatDeSante())) {
                satisfaction += 5;
            } else {
                satisfaction -= 5;
            }
        } else {
            System.out.println(nom + " doit d'abord acheter un billet");
        }
    }

    public void acheterBillet() {
        if (!aAcheteBillet) {
            this.billet = age < 12 ? "Enfant" : "Adulte";
            this.aAcheteBillet = true;
            System.out.println(nom + " a acheté un billet " + billet);
        } else {
            System.out.println(nom + " a déjà un billet");
        }
    }

    public double calculerPrixBillet() {
        return age < 12 ? PRIX_BILLET_ENFANT : PRIX_BILLET_ADULTE;
    }

    // Getters avec annotations Jackson
    @JsonProperty("nom")
    public String getNom() { return nom; }

    @JsonProperty("age")
    public int getAge() { return age; }

    @JsonProperty("billet")
    public String getBillet() { return billet; }

    @JsonProperty("satisfaction")
    public double getSatisfaction() { return satisfaction; }

    @JsonProperty("nombreAnimauxObserves")
    public int getNombreAnimauxObserves() { return nombreAnimauxObserves; }

    @JsonProperty("aAcheteBillet")
    public boolean aAcheteBillet() { return aAcheteBillet; }

    @Override
    public String toString() {
        return String.format("%s (%d ans) - Billet: %s - Satisfaction: %.1f%%",
            nom, age, billet != null ? billet : "Aucun", satisfaction);
    }
} 