package com.example.evaluation_wallois_arthur.models.employes;

public abstract class Employe {
    protected String nom;
    protected String poste;
    protected double salaire;
    protected boolean enRepos;
    protected int joursDeReposRestants;
    protected double niveauSante;
    protected int joursSansPause;

    public Employe(String nom, String poste, double salaire) {
        this.nom = nom;
        this.poste = poste;
        this.salaire = salaire;
        this.enRepos = false;
        this.joursDeReposRestants = 0;
        this.niveauSante = 100.0; // Commence à 100% de santé
        this.joursSansPause = 0;
    }

    public abstract void effectuerTache();

    // Getters
    public String getNom() { return nom; }
    public String getPoste() { return poste; }
    public double getSalaire() { return salaire; }
    public boolean estEnRepos() { return enRepos; }
    public int getJoursDeReposRestants() { return joursDeReposRestants; }
    public double getNiveauSante() { return niveauSante; }
    public int getJoursSansPause() { return joursSansPause; }

    // Setters
    public void setEnRepos(boolean enRepos) { 
        this.enRepos = enRepos;
        if (enRepos) {
            this.joursSansPause = 0; // Réinitialise le compteur quand l'employé part en repos
        }
    }
    
    public void setJoursDeReposRestants(int jours) { 
        this.joursDeReposRestants = jours; 
    }

    public void diminuerJoursDeRepos() {
        if (joursDeReposRestants > 0) {
            joursDeReposRestants--;
            // Récupère 50% de santé par jour de repos
            niveauSante = Math.min(100, niveauSante + 50);
            if (joursDeReposRestants == 0) {
                enRepos = false;
            }
        }
    }

    public void travaillerJournee() {
        if (!enRepos) {
            joursSansPause++;
            // Perte de santé proportionnelle au nombre de jours sans pause
            niveauSante = Math.max(0, niveauSante - (100.0 / 7.0)); // Perd ~14.3% de santé par jour
        }
    }

    public boolean estTropFatigue() {
        return niveauSante <= 0 || joursSansPause >= 7;
    }

    @Override
    public String toString() {
        String status = enRepos ? " [En repos - " + joursDeReposRestants + " jour(s) restant(s)]" : 
                       String.format(" [Santé: %.1f%% - %d jours sans pause]", niveauSante, joursSansPause);
        return String.format("%s - %s - Salaire: %.2f€%s", 
            nom, 
            poste, 
            salaire,
            status);
    }
} 