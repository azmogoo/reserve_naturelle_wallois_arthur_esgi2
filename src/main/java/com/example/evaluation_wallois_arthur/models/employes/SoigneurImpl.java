package com.example.evaluation_wallois_arthur.models.employes;

import com.example.evaluation_wallois_arthur.interfaces.Soigneur;
import com.example.evaluation_wallois_arthur.models.Animal;

public class SoigneurImpl extends Employe implements Soigneur {
    public SoigneurImpl(String nom) {
        super(nom, "Soigneur", 2500.0);
    }

    @Override
    public void nourrir(Animal animal) {
        System.out.println(nom + " nourrit " + animal.getNom());
        animal.manger();
    }

    @Override
    public void soigner(Animal animal) {
        System.out.println(nom + " soigne " + animal.getNom());
        animal.setEtatDeSante("Bon");
    }

    @Override
    public void effectuerTache() {
        System.out.println(nom + " effectue ses tâches quotidiennes");
    }
} 