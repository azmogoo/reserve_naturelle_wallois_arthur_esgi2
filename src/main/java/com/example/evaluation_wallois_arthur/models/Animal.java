package com.example.evaluation_wallois_arthur.models;

import com.example.evaluation_wallois_arthur.interfaces.Deplacable;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS)
public abstract class Animal implements Deplacable {
    protected String nom;
    protected int age;
    protected double poids;
    protected double taille;
    protected String etatDeSante;
    protected int joursDepuisDerniereReproduction;
    protected int joursPassesDansEnclos;
    protected static final int AGE_MINIMUM_REPRODUCTION = 3;
    protected static final int DELAI_ENTRE_REPRODUCTIONS = 30;
    public static final int JOURS_MINIMUM_DANS_ENCLOS = 5;

    @JsonCreator
    public Animal(
        @JsonProperty("nom") String nom,
        @JsonProperty("age") int age,
        @JsonProperty("poids") double poids,
        @JsonProperty("taille") double taille
    ) {
        this.nom = nom;
        this.age = age;
        this.poids = poids;
        this.taille = taille;
        this.etatDeSante = "Bon";
        this.joursDepuisDerniereReproduction = DELAI_ENTRE_REPRODUCTIONS;
        this.joursPassesDansEnclos = 0;
    }

    public void manger() {
        System.out.println(nom + " est en train de manger");
    }

    public abstract void emettreSon();

    public boolean seReproduire(Animal partenaire) {
        if (peutSeReproduireAvec(partenaire)) {
            joursDepuisDerniereReproduction = 0;
            return true;
        }
        return false;
    }

    public boolean estFertile() {
        return age >= AGE_MINIMUM_REPRODUCTION && 
               joursDepuisDerniereReproduction >= DELAI_ENTRE_REPRODUCTIONS &&
               joursPassesDansEnclos >= JOURS_MINIMUM_DANS_ENCLOS &&
               "Bon".equals(etatDeSante);
    }

    public boolean peutSeReproduireAvec(Animal partenaire) {
        return partenaire != null && 
               this.getClass() == partenaire.getClass() &&
               this != partenaire &&
               this.estFertile() &&
               partenaire.estFertile();
    }

    public int getAgeMinimumReproduction() {
        return AGE_MINIMUM_REPRODUCTION;
    }

    public int getDelaiEntreReproductions() {
        return DELAI_ENTRE_REPRODUCTIONS;
    }

    public void incrementerJoursDepuisReproduction() {
        if (joursDepuisDerniereReproduction < DELAI_ENTRE_REPRODUCTIONS) {
            joursDepuisDerniereReproduction++;
        }
    }

    public void incrementerJoursPassesDansEnclos() {
        this.joursPassesDansEnclos++;
    }

    public void reinitialiserJoursPassesDansEnclos() {
        this.joursPassesDansEnclos = 0;
    }

    public int getJoursPassesDansEnclos() {
        return joursPassesDansEnclos;
    }

    // Getters et setters essentiels
    @JsonProperty("nom")
    public String getNom() { return nom; }
    
    @JsonProperty("age")
    public int getAge() { return age; }
    
    public void setAge(int age) { 
        this.age = age;
        if (age > 20) {
            etatDeSante = "Fragile";
        }
    }
    
    @JsonProperty("poids")
    public double getPoids() { return poids; }
    
    @JsonProperty("taille")
    public double getTaille() { return taille; }
    
    @JsonProperty("etatDeSante")
    public String getEtatDeSante() { return etatDeSante; }
    
    public void setEtatDeSante(String etatDeSante) { this.etatDeSante = etatDeSante; }

    @Override
    public String toString() {
        return String.format("%s %s - %d ans - %.1f kg - État: %s", 
            getClass().getSimpleName(),
            nom, 
            age, 
            poids, 
            etatDeSante);
    }
} 