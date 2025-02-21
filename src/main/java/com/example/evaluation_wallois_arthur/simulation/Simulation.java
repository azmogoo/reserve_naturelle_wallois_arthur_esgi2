package com.example.evaluation_wallois_arthur.simulation;

import com.example.evaluation_wallois_arthur.models.Animal;
import com.example.evaluation_wallois_arthur.models.habitats.Habitat;
import com.example.evaluation_wallois_arthur.models.employes.Employe;
import com.example.evaluation_wallois_arthur.models.employes.SoigneurImpl;
import com.example.evaluation_wallois_arthur.models.employes.Veterinaire;
import com.example.evaluation_wallois_arthur.models.Visiteur;
import com.example.evaluation_wallois_arthur.models.StatistiquesReserve;
import com.example.evaluation_wallois_arthur.models.Incident;
import com.example.evaluation_wallois_arthur.interfaces.Reproductible;
import com.example.evaluation_wallois_arthur.interfaces.Mangeable;
import com.example.evaluation_wallois_arthur.models.animaux.Lion;
import com.example.evaluation_wallois_arthur.models.animaux.Aigle;
import com.example.evaluation_wallois_arthur.models.animaux.Serpent;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Iterator;

public class Simulation {
    private List<Animal> animaux;
    private List<Habitat> habitats;
    private List<Employe> employes;
    private List<Visiteur> visiteurs;
    private StatistiquesReserve statistiques;
    private List<String> activitesVisiteurs;
    private int jour;
    private static final Random random = new Random();
    private static final double BUDGET_INITIAL = 2500.0;
    private double budget;

    public Simulation() {
        this.animaux = new ArrayList<>();
        this.habitats = new ArrayList<>();
        this.employes = new ArrayList<>();
        this.visiteurs = new ArrayList<>();
        this.statistiques = new StatistiquesReserve();
        this.activitesVisiteurs = new ArrayList<>();
        this.jour = 1;
        this.budget = BUDGET_INITIAL;
    }

    private int calculerNombreVisiteurs() {
        if (animaux.isEmpty()) {
            return 0; // Pas de visiteurs s'il n'y a pas d'animaux
        }

        // Base de visiteurs en fonction du nombre d'animaux (2-5 visiteurs par animal)
        int baseVisiteurs = animaux.size() * (2 + random.nextInt(4));

        // Facteur de satisfaction (0.5 à 1.5)
        double facteurSatisfaction = (statistiques.getSatisfactionVisiteurs() / 100.0) * 1.5;
        if (facteurSatisfaction < 0.5) facteurSatisfaction = 0.5;

        // Facteur état des animaux
        double facteurSanteAnimaux = calculerFacteurSanteAnimaux();

        // Facteur état des enclos
        double facteurPropreteEnclos = calculerFacteurPropreteEnclos();

        // Calcul final avec tous les facteurs
        int nombreVisiteurs = (int) (baseVisiteurs * facteurSatisfaction * facteurSanteAnimaux * facteurPropreteEnclos);
        
        // Minimum 1 visiteur si il y a des animaux, maximum 30 visiteurs par jour
        return Math.min(Math.max(nombreVisiteurs, 1), 30);
    }

    private double calculerFacteurSanteAnimaux() {
        if (animaux.isEmpty()) return 0.5;

        int animauxEnBonneSante = 0;
        for (Animal animal : animaux) {
            if ("Bon".equals(animal.getEtatDeSante()) || "Excellent".equals(animal.getEtatDeSante())) {
                animauxEnBonneSante++;
            }
        }

        double tauxSante = (double) animauxEnBonneSante / animaux.size();
        return 0.5 + (tauxSante * 0.5); // Entre 0.5 et 1.0
    }

    private double calculerFacteurPropreteEnclos() {
        if (habitats.isEmpty()) return 0.5;

        double propreteMoyenne = 0;
        for (Habitat habitat : habitats) {
            propreteMoyenne += habitat.getProprete();
        }
        propreteMoyenne /= habitats.size();

        return 0.5 + ((propreteMoyenne / 100.0) * 0.5); // Entre 0.5 et 1.0
    }

    private void mettreAJourSatisfactionVisiteurs() {
        for (Visiteur visiteur : visiteurs) {
            // Impact de la santé des animaux
            double facteurSante = calculerFacteurSanteAnimaux();
            
            // Impact de la propreté des enclos
            double facteurProprete = calculerFacteurPropreteEnclos();
            
            // Calcul de la nouvelle satisfaction
            double nouvelleSatisfaction = visiteur.getSatisfaction();
            nouvelleSatisfaction *= (facteurSante + facteurProprete) / 2;
            
            // Limiter entre 0 et 100
            nouvelleSatisfaction = Math.min(Math.max(nouvelleSatisfaction, 0), 100);
            
            // Mise à jour des statistiques
            statistiques.updateSatisfactionVisiteurs(nouvelleSatisfaction);
        }
    }

    private void ajouterInteractionAnimale(String nomVisiteur, Animal animal) {
        if (random.nextInt(5) == 0) { // Une chance sur 5
            String interaction = "";
            
            if (animal instanceof Lion) {
                interaction = nomVisiteur + " sursaute ! " + animal.getNom() + " rugit puissamment dans sa direction !";
                // Baisse légère de satisfaction
                statistiques.updateSatisfactionVisiteurs(statistiques.getSatisfactionVisiteurs() * 0.95);
            } 
            else if (animal instanceof Aigle) {
                interaction = "Oups ! " + animal.getNom() + " fait un petit cadeau sur la tête de " + nomVisiteur + " !";
                // Baisse modérée de satisfaction
                statistiques.updateSatisfactionVisiteurs(statistiques.getSatisfactionVisiteurs() * 0.90);
            }
            else if (animal instanceof Serpent) {
                interaction = nomVisiteur + " frissonne quand " + animal.getNom() + " tire la langue dans sa direction !";
                // Légère baisse de satisfaction
                statistiques.updateSatisfactionVisiteurs(statistiques.getSatisfactionVisiteurs() * 0.97);
            }
            
            if (!interaction.isEmpty()) {
                ajouterActiviteVisiteur(interaction);
            }
        }
    }

    private void verifierReproductions() {
        for (Habitat habitat : habitats) {
            List<Animal> animauxHabitat = habitat.getAnimaux();
            if (animauxHabitat.size() >= 2 && habitat.peutAccueillirNouvelAnimal()) {
                // Vérifier les paires d'animaux de même espèce
                for (int i = 0; i < animauxHabitat.size() - 1; i++) {
                    for (int j = i + 1; j < animauxHabitat.size(); j++) {
                        Animal animal1 = animauxHabitat.get(i);
                        Animal animal2 = animauxHabitat.get(j);
                        
                        if (animal1.getClass() == animal2.getClass() && 
                            animal1 instanceof Reproductible && 
                            animal2 instanceof Reproductible) {
                            
                            // Vérifier que les deux animaux ont passé assez de temps dans l'enclos
                            if (animal1.getJoursPassesDansEnclos() >= Animal.JOURS_MINIMUM_DANS_ENCLOS &&
                                animal2.getJoursPassesDansEnclos() >= Animal.JOURS_MINIMUM_DANS_ENCLOS) {
                                
                                Reproductible parent1 = (Reproductible) animal1;
                                Reproductible parent2 = (Reproductible) animal2;
                                
                                if (parent1.peutSeReproduireAvec(animal2) && 
                                    parent2.peutSeReproduireAvec(animal1) &&
                                    parent1.estFertile() && parent2.estFertile()) {
                                    
                                    // Création du bébé en fonction du type des parents
                                    Animal bebe = null;
                                    String nomBebe = demanderNomBebe(animal1.getClass().getSimpleName());
                                    
                                    if (animal1 instanceof Lion) {
                                        bebe = new Lion(nomBebe, 0, 10.0, 0.3); // Petit lion
                                    } else if (animal1 instanceof Aigle) {
                                        bebe = new Aigle(nomBebe, 0, 0.5, 0.2); // Petit aigle
                                    } else if (animal1 instanceof Serpent) {
                                        bebe = new Serpent(nomBebe, 0, 0.3, 0.1); // Petit serpent
                                    }
                                    
                                    if (bebe != null) {
                                        habitat.ajouterAnimal(bebe);
                                        animaux.add(bebe);
                                        statistiques.enregistrerNaissance(bebe.getClass().getSimpleName());
                                        ajouterActiviteVisiteur("NAISSANCE : Un bébé " + bebe.getClass().getSimpleName() + 
                                                              " nommé " + nomBebe + " est né !");
                                        
                                        // Réinitialiser le compteur de reproduction pour les deux parents
                                        parent1.seReproduire(animal2);
                                        parent2.seReproduire(animal1);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private String demanderNomBebe(String especeAnimal) {
        // Cette méthode sera appelée par le contrôleur pour afficher une boîte de dialogue
        // Pour l'instant, on retourne un nom par défaut
        return "Bébé" + especeAnimal + (animaux.size() + 1);
    }

    public void passerJournee() {
        jour++;
        System.out.println("\n=== Jour " + jour + " ===");
        
        // Événements aléatoires
        for (Animal animal : animaux) {
            if (random.nextDouble() < 0.1) {
                animal.setEtatDeSante("Malade");
                System.out.println(animal.getNom() + " est tombé malade !");
            }
            // Incrémenter le compteur de jours depuis la dernière reproduction
            if (animal instanceof Reproductible) {
                ((Reproductible) animal).incrementerJoursDepuisReproduction();
            }
            // Incrémenter le compteur de jours dans l'enclos
            animal.incrementerJoursPassesDansEnclos();
        }

        // Vérifier les reproductions possibles
        verifierReproductions();

        // Fatigue des employés
        for (Employe employe : employes) {
            employe.effectuerTache();
        }

        // Mise à jour de la satisfaction des visiteurs existants
        mettreAJourSatisfactionVisiteurs();

        // Génération de nouveaux visiteurs en fonction des facteurs
        int nombreNouveauxVisiteurs = calculerNombreVisiteurs();
        for (int i = 0; i < nombreNouveauxVisiteurs; i++) {
            String nomVisiteur = "Visiteur" + (visiteurs.size() + 1);
            int ageVisiteur = random.nextInt(70) + 5; // Âge entre 5 et 75 ans
            Visiteur nouveauVisiteur = new Visiteur(nomVisiteur, ageVisiteur);
            nouveauVisiteur.acheterBillet();
            
            // Ajouter le revenu du billet aux statistiques
            double prixBillet = nouveauVisiteur.calculerPrixBillet();
            budget += prixBillet;
            statistiques.ajouterRecette(prixBillet);
            
            // Observer des animaux aléatoires
            if (!animaux.isEmpty()) {
                int nombreObservations = random.nextInt(3) + 1; // 1 à 3 observations
                for (int j = 0; j < nombreObservations; j++) {
                    Animal animalObserve = animaux.get(random.nextInt(animaux.size()));
                    nouveauVisiteur.observerAnimal(animalObserve);
                    ajouterActiviteVisiteur(nomVisiteur + " observe " + animalObserve.getNom());
                    
                    // Ajouter une possible interaction amusante
                    ajouterInteractionAnimale(nomVisiteur, animalObserve);
                }
            }
            
            visiteurs.add(nouveauVisiteur);
            statistiques.ajouterVisiteur();
        }

        // Mise à jour des statistiques
        statistiques.incrementerJour();
        nourrirAnimaux();
    }

    public void nourrirAnimaux() {
        for (Animal animal : animaux) {
            if (animal instanceof Mangeable) {
                ((Mangeable) animal).manger();
            }
        }
    }

    // Getters
    public List<Animal> getAnimaux() {
        return animaux;
    }

    public List<Habitat> getHabitats() {
        return habitats;
    }

    public List<Employe> getEmployes() {
        return employes;
    }

    public List<Visiteur> getVisiteurs() {
        return visiteurs;
    }

    public StatistiquesReserve getStatistiques() {
        return statistiques;
    }

    public List<String> getActivitesVisiteurs() {
        return activitesVisiteurs;
    }

    public int getJour() {
        return jour;
    }

    public double getBudget() {
        return budget;
    }

    // Méthodes pour ajouter des éléments à la simulation
    public boolean ajouterAnimal(Animal animal) {
        return animaux.add(animal);
    }

    public boolean ajouterHabitat(Habitat habitat) {
        if (budget >= habitat.getPrix()) {
            if (habitats.add(habitat)) {
                budget -= habitat.getPrix();
                System.out.println("Habitat construit pour " + habitat.getPrix() + "€");
                return true;
            }
        }
        return false;
    }

    public boolean ajouterEmploye(Employe employe) {
        return employes.add(employe);
    }

    public void ajouterVisiteur(Visiteur visiteur) {
        visiteurs.add(visiteur);
    }

    public void ajouterActiviteVisiteur(String activite) {
        activitesVisiteurs.add(activite);
    }
} 