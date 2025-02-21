package com.example.evaluation_wallois_arthur.models;

import java.util.HashMap;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Classe gérant les statistiques de la réserve naturelle
 * @author Arthur Wallois
 */
public class StatistiquesReserve {
    private int nombreVisiteurs;
    private double satisfactionVisiteurs;
    private double revenuTotal;
    private int nombreIncidents;
    private Map<String, Integer> naissances;

    @JsonCreator
    public StatistiquesReserve() {
        this.nombreVisiteurs = 0;
        this.satisfactionVisiteurs = 100.0;
        this.revenuTotal = 0.0;
        this.nombreIncidents = 0;
        this.naissances = new HashMap<>();
    }

    public void ajouterVisiteur() {
        nombreVisiteurs++;
    }

    public void ajouterRecette(double montant) {
        revenuTotal += montant;
    }

    public void updateSatisfactionVisiteurs(double nouvelleSatisfaction) {
        this.satisfactionVisiteurs = nouvelleSatisfaction;
    }

    public void ajouterIncident() {
        nombreIncidents++;
        satisfactionVisiteurs = Math.max(0, satisfactionVisiteurs - 10);
    }

    public void enregistrerNaissance(String espece) {
        naissances.put(espece, naissances.getOrDefault(espece, 0) + 1);
    }

    public void incrementerJour() {
        // Mise à jour quotidienne des statistiques si nécessaire
    }

    // Getters avec annotations Jackson
    @JsonProperty("nombreVisiteurs")
    public int getNombreVisiteurs() { return nombreVisiteurs; }

    @JsonProperty("satisfactionVisiteurs")
    public double getSatisfactionVisiteurs() { return satisfactionVisiteurs; }

    @JsonProperty("revenuTotal")
    public double getRevenuTotal() { return revenuTotal; }

    @JsonProperty("nombreIncidents")
    public int getNombreIncidents() { return nombreIncidents; }

    @JsonProperty("naissances")
    public Map<String, Integer> getNaissances() { return naissances; }

    public double getBilan() {
        return revenuTotal;
    }

    /**
     * Génère un rapport complet des statistiques
     * @return Le rapport sous forme de chaîne de caractères
     */
    public String genererRapport() {
        StringBuilder rapport = new StringBuilder();
        rapport.append("=== RAPPORT STATISTIQUES ===\n\n");
        
        rapport.append("DÉMOGRAPHIE\n");
        rapport.append("Nombre total: ").append(nombreVisiteurs).append("\n");
        rapport.append("Satisfaction moyenne: ").append(String.format("%.1f%%\n", satisfactionVisiteurs));
        rapport.append("\n");
        
        rapport.append("FINANCES\n");
        rapport.append("Revenu total: ").append(String.format("%.2f€\n", revenuTotal));

        return rapport.toString();
    }
} 