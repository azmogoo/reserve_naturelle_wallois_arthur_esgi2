package com.example.evaluation_wallois_arthur.models.employes;

import com.example.evaluation_wallois_arthur.interfaces.Soigneur;
import com.example.evaluation_wallois_arthur.models.Animal;

public class Veterinaire extends Employe implements Soigneur {
    public Veterinaire(String nom) {
        super(nom, "Vétérinaire", 3500.0);
    }

    @Override
    public void nourrir(Animal animal) {
        System.out.println(nom + " administre une alimentation spéciale à " + animal.getNom());
        animal.manger();
    }

    @Override
    public void soigner(Animal animal) {
        System.out.println(nom + " effectue un examen médical sur " + animal.getNom());
        animal.setEtatDeSante("Excellent");
    }

    @Override
    public void effectuerTache() {
        System.out.println(nom + " fait sa tournée d'inspection médicale");
    }

    public void effectuerOperation(Animal animal) {
        System.out.println(nom + " effectue une opération sur " + animal.getNom());
    }
} 