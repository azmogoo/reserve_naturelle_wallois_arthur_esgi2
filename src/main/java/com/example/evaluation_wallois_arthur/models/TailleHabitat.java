package com.example.evaluation_wallois_arthur.models;

public enum TailleHabitat {
    PETIT(3, 250.0, "Petit"),
    MOYEN(6, 400.0, "Moyen"),
    GRAND(10, 600.0, "Grand");

    private final int capaciteMax;
    private final double prix;
    private final String description;

    TailleHabitat(int capaciteMax, double prix, String description) {
        this.capaciteMax = capaciteMax;
        this.prix = prix;
        this.description = description;
    }

    public int getCapaciteMax() {
        return capaciteMax;
    }

    public double getPrix() {
        return prix;
    }

    public String getDescription() {
        return description;
    }
} 