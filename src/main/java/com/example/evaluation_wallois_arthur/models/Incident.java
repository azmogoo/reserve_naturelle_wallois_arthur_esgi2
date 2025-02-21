package com.example.evaluation_wallois_arthur.models;

import java.time.LocalDateTime;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Classe représentant un incident dans la réserve
 * @author Arthur Wallois
 */
public class Incident {
    public enum TypeIncident {
        MALADIE("Maladie"),
        BLESSURE("Blessure"),
        CONFLIT("Conflit entre animaux"),
        EVASION("Tentative d'évasion"),
        INFRASTRUCTURE("Problème d'infrastructure"),
        NOURRITURE("Problème de nourriture"),
        AUTRE("Autre incident");

        private final String description;

        TypeIncident(String description) {
            this.description = description;
        }

        public String getDescription() {
            return description;
        }
    }

    public enum NiveauGravite {
        FAIBLE(1),
        MOYEN(2),
        ELEVE(3),
        CRITIQUE(4);

        private final int niveau;

        NiveauGravite(int niveau) {
            this.niveau = niveau;
        }

        public int getNiveau() {
            return niveau;
        }
    }

    private final TypeIncident type;
    private final String description;
    private final LocalDateTime dateHeure;
    private final NiveauGravite gravite;
    private final String lieuIncident;
    private boolean resolu;
    private String solutionApportee;
    private double coutResolution;

    @JsonCreator
    public Incident(
        @JsonProperty("type") TypeIncident type,
        @JsonProperty("description") String description,
        @JsonProperty("gravite") NiveauGravite gravite,
        @JsonProperty("lieuIncident") String lieuIncident
    ) {
        this.type = type;
        this.description = description;
        this.dateHeure = LocalDateTime.now();
        this.gravite = gravite;
        this.lieuIncident = lieuIncident;
        this.resolu = false;
        this.solutionApportee = "";
        this.coutResolution = 0.0;
    }

    /**
     * Résout l'incident
     * @param solution Description de la solution apportée
     * @param cout Coût de la résolution
     */
    public void resoudre(String solution, double cout) {
        this.resolu = true;
        this.solutionApportee = solution;
        this.coutResolution = cout;
    }

    /**
     * Calcule la priorité de l'incident
     * @return Un score de priorité basé sur la gravité et le type
     */
    public int calculerPriorite() {
        int prioriteBase = gravite.getNiveau() * 10;
        
        // Ajustement selon le type d'incident
        switch (type) {
            case MALADIE:
            case BLESSURE:
                return prioriteBase + 5;
            case EVASION:
                return prioriteBase + 8;
            case INFRASTRUCTURE:
                return prioriteBase + 3;
            default:
                return prioriteBase;
        }
    }

    // Getters avec annotations Jackson
    @JsonProperty("type")
    public TypeIncident getType() { return type; }

    @JsonProperty("description")
    public String getDescription() { return description; }

    @JsonProperty("dateHeure")
    public LocalDateTime getDateHeure() { return dateHeure; }

    @JsonProperty("gravite")
    public NiveauGravite getGravite() { return gravite; }

    @JsonProperty("lieuIncident")
    public String getLieuIncident() { return lieuIncident; }

    @JsonProperty("resolu")
    public boolean isResolu() { return resolu; }

    @JsonProperty("solutionApportee")
    public String getSolutionApportee() { return solutionApportee; }

    @JsonProperty("coutResolution")
    public double getCoutResolution() { return coutResolution; }

    @Override
    public String toString() {
        return String.format("[%s] %s - %s\nLieu: %s\nGravité: %s\nStatut: %s",
            dateHeure.toString(),
            type.getDescription(),
            description,
            lieuIncident,
            gravite.name(),
            resolu ? "Résolu - " + solutionApportee : "Non résolu"
        );
    }
} 