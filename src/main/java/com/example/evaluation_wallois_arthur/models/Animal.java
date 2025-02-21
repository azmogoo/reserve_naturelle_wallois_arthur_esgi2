package com.example.evaluation_wallois_arthur.models;

import com.example.evaluation_wallois_arthur.interfaces.Deplacable;
import com.example.evaluation_wallois_arthur.interfaces.Reproductible;
import com.example.evaluation_wallois_arthur.interfaces.Mangeable;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS)
public abstract class Animal implements Deplacable, Reproductible, Mangeable {
    protected String nom;
    protected int age;
    protected double poids;
    protected double taille;
    protected String etatDeSante;
    protected double niveauFaim;
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
        this.niveauFaim = 100.0; // 100 = rassasié, 0 = affamé
        this.joursDepuisDerniereReproduction = DELAI_ENTRE_REPRODUCTIONS;
        this.joursPassesDansEnclos = 0;
    }

    public void manger() {
        System.out.println(nom + " est en train de manger");
        niveauFaim = 100.0;
    }

    public void dormir() {
        System.out.println(nom + " dort paisiblement");
        // Récupération d'un peu de santé pendant le sommeil
        if (!"Bon".equals(etatDeSante)) {
            etatDeSante = "Fatigué";
        }
    }

    public void seDeplacer() {
        System.out.println(nom + " se déplace");
        // Le déplacement consomme de l'énergie
        niveauFaim -= 10;
    }

    public abstract void emettreSon();

    @Override
    public boolean seReproduire(Animal partenaire) {
        if (peutSeReproduireAvec(partenaire)) {
            joursDepuisDerniereReproduction = 0;
            return true;
        }
        return false;
    }

    @Override
    public boolean estFertile() {
        return age >= AGE_MINIMUM_REPRODUCTION && 
               joursDepuisDerniereReproduction >= DELAI_ENTRE_REPRODUCTIONS &&
               joursPassesDansEnclos >= JOURS_MINIMUM_DANS_ENCLOS &&
               "Bon".equals(etatDeSante);
    }

    @Override
    public boolean peutSeReproduireAvec(Animal partenaire) {
        return partenaire != null && 
               this.getClass() == partenaire.getClass() &&
               this != partenaire &&
               this.estFertile() &&
               partenaire.estFertile();
    }

    @Override
    public int getAgeMinimumReproduction() {
        return AGE_MINIMUM_REPRODUCTION;
    }

    @Override
    public int getDelaiEntreReproductions() {
        return DELAI_ENTRE_REPRODUCTIONS;
    }

    @Override
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

    // Getters et setters
    @JsonProperty("nom")
    public String getNom() { return nom; }
    
    @JsonProperty("age")
    public int getAge() { return age; }
    
    public void setAge(int age) { 
        this.age = age;
        // L'animal devient plus fragile avec l'âge
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

    @JsonProperty("niveauFaim")
    public double getNiveauFaim() { return niveauFaim; }

    public void setNiveauFaim(double niveauFaim) { 
        this.niveauFaim = niveauFaim;
        if (niveauFaim < 20) {
            etatDeSante = "Affamé";
        }
    }

    @Override
    public String toString() {
        return String.format("%s %s - %d ans - %.1f kg - État: %s - Faim: %.1f%%", 
            getClass().getSimpleName(),
            nom, 
            age, 
            poids, 
            etatDeSante,
            niveauFaim);
    }
} 