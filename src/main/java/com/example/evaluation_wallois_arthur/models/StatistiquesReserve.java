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
    private Map<String, Integer> naissancesParEspece;
    private Map<String, Integer> decesParEspece;
    private Map<String, Integer> maladiesParEspece;
    private Map<String, Double> tauxOccupationHabitats;
    private double satisfactionMoyenneVisiteurs;
    private int nombreTotalVisiteurs;
    private double revenuTotal;
    private int nombreIncidents;
    private double tauxMortalite;
    private double tauxNatalite;
    private Map<String, Integer> interventionsVeterinaires;
    private Map<String, Double> coutsMaintenance;
    private double satisfactionVisiteurs;
    private int nombreTotalAnimaux;
    private int nombreTotalHabitats;
    private int nombreTotalEmployes;
    private int nombreTotalNaissances;
    private int nombreTotalDeces;
    private int nombreTotalMaladies;
    private int nombreTotalIncidents;
    private int nombreJours;
    private int nombreVisiteurs;
    private double recettes;
    private double depenses;

    @JsonCreator
    public StatistiquesReserve() {
        this.naissancesParEspece = new HashMap<>();
        this.decesParEspece = new HashMap<>();
        this.maladiesParEspece = new HashMap<>();
        this.tauxOccupationHabitats = new HashMap<>();
        this.satisfactionMoyenneVisiteurs = 100.0;
        this.nombreTotalVisiteurs = 0;
        this.revenuTotal = 0.0;
        this.nombreIncidents = 0;
        this.tauxMortalite = 0.0;
        this.tauxNatalite = 0.0;
        this.interventionsVeterinaires = new HashMap<>();
        this.coutsMaintenance = new HashMap<>();
        this.satisfactionVisiteurs = 100.0;
        this.nombreTotalAnimaux = 0;
        this.nombreTotalHabitats = 0;
        this.nombreTotalEmployes = 0;
        this.nombreTotalNaissances = 0;
        this.nombreTotalDeces = 0;
        this.nombreTotalMaladies = 0;
        this.nombreTotalIncidents = 0;
        this.nombreJours = 0;
        this.nombreVisiteurs = 0;
        this.recettes = 0;
        this.depenses = 0;
    }

    /**
     * Enregistre une naissance
     * @param espece L'espèce de l'animal né
     */
    public void enregistrerNaissance(String espece) {
        naissancesParEspece.merge(espece, 1, Integer::sum);
        calculerTauxNatalite();
        nombreTotalNaissances++;
    }

    /**
     * Enregistre un décès
     * @param espece L'espèce de l'animal décédé
     */
    public void enregistrerDeces(String espece) {
        decesParEspece.merge(espece, 1, Integer::sum);
        calculerTauxMortalite();
        nombreTotalDeces++;
    }

    /**
     * Enregistre une maladie
     * @param espece L'espèce de l'animal malade
     */
    public void enregistrerMaladie(String espece) {
        maladiesParEspece.merge(espece, 1, Integer::sum);
        nombreTotalMaladies++;
    }

    /**
     * Met à jour le taux d'occupation d'un habitat
     * @param nomHabitat Le nom de l'habitat
     * @param tauxOccupation Le taux d'occupation (0-100)
     */
    public void updateTauxOccupation(String nomHabitat, double tauxOccupation) {
        tauxOccupationHabitats.put(nomHabitat, tauxOccupation);
        nombreTotalHabitats++;
    }

    /**
     * Met à jour la satisfaction moyenne des visiteurs
     * @param satisfaction La nouvelle satisfaction (0-100)
     */
    public void updateSatisfactionVisiteurs(double satisfaction) {
        this.satisfactionVisiteurs = satisfaction;
    }

    /**
     * Enregistre une intervention vétérinaire
     * @param typeIntervention Le type d'intervention
     */
    public void enregistrerInterventionVeterinaire(String typeIntervention) {
        interventionsVeterinaires.merge(typeIntervention, 1, Integer::sum);
    }

    /**
     * Ajoute un coût de maintenance
     * @param type Le type de maintenance
     * @param cout Le coût en euros
     */
    public void ajouterCoutMaintenance(String type, double cout) {
        coutsMaintenance.merge(type, cout, Double::sum);
    }

    /**
     * Ajoute un revenu aux statistiques
     * @param montant Le montant à ajouter aux revenus
     */
    public void ajouterRevenu(double montant) {
        this.revenuTotal += montant;
    }

    public void incrementerJour() {
        this.nombreJours++;
    }

    public void ajouterVisiteur() {
        this.nombreVisiteurs++;
    }

    public void ajouterIncident() {
        this.nombreIncidents++;
    }

    public void ajouterRecette(double montant) {
        this.recettes += montant;
    }

    public void ajouterDepense(double montant) {
        this.depenses += montant;
    }

    private void calculerTauxMortalite() {
        int totalDeces = decesParEspece.values().stream().mapToInt(Integer::intValue).sum();
        int totalAnimaux = naissancesParEspece.values().stream().mapToInt(Integer::intValue).sum();
        this.tauxMortalite = totalAnimaux > 0 ? (double) totalDeces / totalAnimaux * 100 : 0;
    }

    private void calculerTauxNatalite() {
        int totalNaissances = naissancesParEspece.values().stream().mapToInt(Integer::intValue).sum();
        int totalAnimaux = naissancesParEspece.values().stream().mapToInt(Integer::intValue).sum();
        this.tauxNatalite = totalAnimaux > 0 ? (double) totalNaissances / totalAnimaux * 100 : 0;
    }

    // Getters avec annotations Jackson
    @JsonProperty("naissancesParEspece")
    public Map<String, Integer> getNaissancesParEspece() { return naissancesParEspece; }

    @JsonProperty("decesParEspece")
    public Map<String, Integer> getDecesParEspece() { return decesParEspece; }

    @JsonProperty("maladiesParEspece")
    public Map<String, Integer> getMaladiesParEspece() { return maladiesParEspece; }

    @JsonProperty("tauxOccupationHabitats")
    public Map<String, Double> getTauxOccupationHabitats() { return tauxOccupationHabitats; }

    @JsonProperty("satisfactionMoyenneVisiteurs")
    public double getSatisfactionMoyenneVisiteurs() { return satisfactionMoyenneVisiteurs; }

    @JsonProperty("nombreTotalVisiteurs")
    public int getNombreTotalVisiteurs() { return nombreTotalVisiteurs; }

    @JsonProperty("revenuTotal")
    public double getRevenuTotal() { return revenuTotal; }

    @JsonProperty("nombreIncidents")
    public int getNombreIncidents() { return nombreIncidents; }

    @JsonProperty("tauxMortalite")
    public double getTauxMortalite() { return tauxMortalite; }

    @JsonProperty("tauxNatalite")
    public double getTauxNatalite() { return tauxNatalite; }

    @JsonProperty("interventionsVeterinaires")
    public Map<String, Integer> getInterventionsVeterinaires() { return interventionsVeterinaires; }

    @JsonProperty("coutsMaintenance")
    public Map<String, Double> getCoutsMaintenance() { return coutsMaintenance; }

    @JsonProperty("satisfactionVisiteurs")
    public double getSatisfactionVisiteurs() { return satisfactionVisiteurs; }

    @JsonProperty("nombreTotalAnimaux")
    public int getNombreTotalAnimaux() { return nombreTotalAnimaux; }

    @JsonProperty("nombreTotalHabitats")
    public int getNombreTotalHabitats() { return nombreTotalHabitats; }

    @JsonProperty("nombreTotalEmployes")
    public int getNombreTotalEmployes() { return nombreTotalEmployes; }

    @JsonProperty("nombreTotalNaissances")
    public int getNombreTotalNaissances() { return nombreTotalNaissances; }

    @JsonProperty("nombreTotalDeces")
    public int getNombreTotalDeces() { return nombreTotalDeces; }

    @JsonProperty("nombreTotalMaladies")
    public int getNombreTotalMaladies() { return nombreTotalMaladies; }

    @JsonProperty("nombreTotalIncidents")
    public int getNombreTotalIncidents() { return nombreTotalIncidents; }

    public int getNombreJours() {
        return nombreJours;
    }

    public int getNombreVisiteurs() {
        return nombreVisiteurs;
    }

    public double getRecettes() {
        return recettes;
    }

    public double getDepenses() {
        return depenses;
    }

    public double getBilan() {
        return recettes - depenses;
    }

    /**
     * Génère un rapport complet des statistiques
     * @return Le rapport sous forme de chaîne de caractères
     */
    public String genererRapport() {
        StringBuilder rapport = new StringBuilder();
        rapport.append("=== RAPPORT STATISTIQUES ===\n\n");
        
        rapport.append("DÉMOGRAPHIE\n");
        rapport.append("Taux de natalité: ").append(String.format("%.1f%%\n", tauxNatalite));
        rapport.append("Taux de mortalité: ").append(String.format("%.1f%%\n\n", tauxMortalite));
        
        rapport.append("NAISSANCES PAR ESPÈCE\n");
        naissancesParEspece.forEach((espece, nombre) -> 
            rapport.append(espece).append(": ").append(nombre).append("\n"));
        rapport.append("\n");
        
        rapport.append("SANTÉ\n");
        rapport.append("Nombre d'incidents: ").append(nombreIncidents).append("\n");
        rapport.append("Interventions vétérinaires:\n");
        interventionsVeterinaires.forEach((type, nombre) -> 
            rapport.append("- ").append(type).append(": ").append(nombre).append("\n"));
        rapport.append("\n");
        
        rapport.append("VISITEURS\n");
        rapport.append("Nombre total: ").append(nombreTotalVisiteurs).append("\n");
        rapport.append("Satisfaction moyenne: ").append(String.format("%.1f%%\n", satisfactionVisiteurs));
        rapport.append("\n");
        
        rapport.append("FINANCES\n");
        rapport.append("Revenu total: ").append(String.format("%.2f€\n", revenuTotal));
        rapport.append("Coûts de maintenance:\n");
        coutsMaintenance.forEach((type, cout) -> 
            rapport.append("- ").append(type).append(": ").append(String.format("%.2f€\n", cout)));

        return rapport.toString();
    }
} 