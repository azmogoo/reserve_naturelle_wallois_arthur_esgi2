package com.example.evaluation_wallois_arthur.models.employes;

public abstract class Employe {
    protected String nom;
    protected String poste;
    protected double salaire;
    protected boolean enRepos;
    protected int joursDeReposRestants;

    public Employe(String nom, String poste, double salaire) {
        this.nom = nom;
        this.poste = poste;
        this.salaire = salaire;
        this.enRepos = false;
        this.joursDeReposRestants = 0;
    }

    public abstract void effectuerTache();

    // Getters
    public String getNom() { return nom; }
    public String getPoste() { return poste; }
    public double getSalaire() { return salaire; }
    public boolean estEnRepos() { return enRepos; }
    public int getJoursDeReposRestants() { return joursDeReposRestants; }

    // Setters
    public void setEnRepos(boolean enRepos) { this.enRepos = enRepos; }
    public void setJoursDeReposRestants(int jours) { this.joursDeReposRestants = jours; }

    public void diminuerJoursDeRepos() {
        if (joursDeReposRestants > 0) {
            joursDeReposRestants--;
            if (joursDeReposRestants == 0) {
                enRepos = false;
            }
        }
    }

    @Override
    public String toString() {
        String status = enRepos ? " [En repos - " + joursDeReposRestants + " jour(s) restant(s)]" : "";
        return String.format("%s - %s - Salaire: %.2f€%s", 
            nom, 
            poste, 
            salaire,
            status);
    }
} 