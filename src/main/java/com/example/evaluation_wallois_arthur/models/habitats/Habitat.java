package com.example.evaluation_wallois_arthur.models.habitats;

import com.example.evaluation_wallois_arthur.models.Animal;
import com.example.evaluation_wallois_arthur.models.TailleHabitat;
import java.util.ArrayList;
import java.util.List;

public abstract class Habitat {
    protected String nom;
    protected TailleHabitat taille;
    protected List<Animal> animaux;
    protected double proprete;
    protected double prix;

    public Habitat(String nom, TailleHabitat taille) {
        this.nom = nom;
        this.taille = taille;
        this.animaux = new ArrayList<>();
        this.proprete = 100.0;
        this.prix = taille.getPrix();
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public TailleHabitat getTaille() {
        return taille;
    }

    public List<Animal> getAnimaux() {
        return animaux;
    }

    public double getProprete() {
        return proprete;
    }

    public void setProprete(double proprete) {
        this.proprete = proprete;
    }

    public void nettoyer() {
        this.proprete = 100.0;
    }

    public boolean peutAccueillirNouvelAnimal() {
        return animaux.size() < taille.getCapaciteMax();
    }

    public boolean ajouterAnimal(Animal animal) {
        if (peutAccueillirNouvelAnimal()) {
            animal.reinitialiserJoursPassesDansEnclos();
            return animaux.add(animal);
        }
        return false;
    }

    public double getPrix() {
        return prix;
    }

    @Override
    public String toString() {
        return nom + " (" + taille.getDescription() + ") - " + 
               animaux.size() + "/" + taille.getCapaciteMax() + " animaux";
    }
} 