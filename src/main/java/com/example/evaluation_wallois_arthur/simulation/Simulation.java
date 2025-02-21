package com.example.evaluation_wallois_arthur.simulation;

import com.example.evaluation_wallois_arthur.models.Animal;
import com.example.evaluation_wallois_arthur.models.habitats.Habitat;
import com.example.evaluation_wallois_arthur.models.employes.Employe;
import com.example.evaluation_wallois_arthur.models.employes.SoigneurImpl;
import com.example.evaluation_wallois_arthur.models.employes.Veterinaire;
import com.example.evaluation_wallois_arthur.models.Visiteur;
import com.example.evaluation_wallois_arthur.models.StatistiquesReserve;
import com.example.evaluation_wallois_arthur.models.Incident;
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
        if (animaux.isEmpty()) return 0;

        int baseVisiteurs = animaux.size() * (2 + random.nextInt(4));
        double facteurSatisfaction = Math.max(0.5, (statistiques.getSatisfactionVisiteurs() / 100.0) * 1.5);
        double facteurSanteAnimaux = calculerFacteurSanteAnimaux();
        double facteurPropreteEnclos = calculerFacteurPropreteEnclos();

        return Math.min(Math.max((int)(baseVisiteurs * facteurSatisfaction * facteurSanteAnimaux * facteurPropreteEnclos), 1), 30);
    }

    private double calculerFacteurSanteAnimaux() {
        if (animaux.isEmpty()) return 0.5;
        long animauxEnBonneSante = animaux.stream()
            .filter(a -> "Bon".equals(a.getEtatDeSante()) || "Excellent".equals(a.getEtatDeSante()))
            .count();
        return 0.5 + ((double) animauxEnBonneSante / animaux.size() * 0.5);
    }

    private double calculerFacteurPropreteEnclos() {
        if (habitats.isEmpty()) return 0.5;
        double propreteMoyenne = habitats.stream()
            .mapToDouble(Habitat::getProprete)
            .average()
            .orElse(0.0);
        return 0.5 + (propreteMoyenne / 100.0 * 0.5);
    }

    private void mettreAJourSatisfactionVisiteurs() {
        double facteurGlobal = (calculerFacteurSanteAnimaux() + calculerFacteurPropreteEnclos()) / 2;
        visiteurs.forEach(visiteur -> {
            double nouvelleSatisfaction = Math.min(Math.max(visiteur.getSatisfaction() * facteurGlobal, 0), 100);
            statistiques.updateSatisfactionVisiteurs(nouvelleSatisfaction);
        });
    }

    private void ajouterInteractionAnimale(String nomVisiteur, Animal animal) {
        if (random.nextInt(5) != 0) return;

        String interaction = creerInteractionAnimale(nomVisiteur, animal);
        if (!interaction.isEmpty()) {
            ajouterActiviteVisiteur(interaction);
        }
    }

    private String creerInteractionAnimale(String nomVisiteur, Animal animal) {
        if (animal instanceof Lion) {
            statistiques.updateSatisfactionVisiteurs(statistiques.getSatisfactionVisiteurs() * 0.95);
            return String.format("%s sursaute ! %s rugit puissamment dans sa direction !", nomVisiteur, animal.getNom());
        } 
        if (animal instanceof Aigle) {
            statistiques.updateSatisfactionVisiteurs(statistiques.getSatisfactionVisiteurs() * 0.90);
            return String.format("Oups ! %s fait un petit cadeau sur la tête de %s !", animal.getNom(), nomVisiteur);
        }
        if (animal instanceof Serpent) {
            statistiques.updateSatisfactionVisiteurs(statistiques.getSatisfactionVisiteurs() * 0.97);
            return String.format("%s frissonne quand %s tire la langue dans sa direction !", nomVisiteur, animal.getNom());
        }
        return "";
    }

    private void verifierReproductions() {
        habitats.stream()
            .filter(h -> h.getAnimaux().size() >= 2 && h.peutAccueillirNouvelAnimal())
            .forEach(this::verifierReproductionsDansHabitat);
    }

    private void verifierReproductionsDansHabitat(Habitat habitat) {
        List<Animal> animauxHabitat = habitat.getAnimaux();
        for (int i = 0; i < animauxHabitat.size() - 1; i++) {
            for (int j = i + 1; j < animauxHabitat.size(); j++) {
                tenterReproduction(animauxHabitat.get(i), animauxHabitat.get(j), habitat);
            }
        }
    }

    private void tenterReproduction(Animal animal1, Animal animal2, Habitat habitat) {
        if (!peuventSeReproduire(animal1, animal2)) return;

        Animal bebe = creerBebe(animal1);
        if (bebe != null) {
            ajouterNouveauNe(bebe, habitat, animal1, animal2);
        }
    }

    private boolean peuventSeReproduire(Animal animal1, Animal animal2) {
        return animal1.getClass() == animal2.getClass() &&
               animal1.getJoursPassesDansEnclos() >= Animal.JOURS_MINIMUM_DANS_ENCLOS &&
               animal2.getJoursPassesDansEnclos() >= Animal.JOURS_MINIMUM_DANS_ENCLOS &&
               animal1.peutSeReproduireAvec(animal2);
    }

    private Animal creerBebe(Animal parent) {
        String nomBebe = demanderNomBebe(parent.getClass().getSimpleName());
        if (parent instanceof Lion) return new Lion(nomBebe, 0, 10.0, 0.3);
        if (parent instanceof Aigle) return new Aigle(nomBebe, 0, 0.5, 0.2);
        if (parent instanceof Serpent) return new Serpent(nomBebe, 0, 0.3, 0.1);
        return null;
    }

    private void ajouterNouveauNe(Animal bebe, Habitat habitat, Animal parent1, Animal parent2) {
        habitat.ajouterAnimal(bebe);
        animaux.add(bebe);
        statistiques.enregistrerNaissance(bebe.getClass().getSimpleName());
        ajouterActiviteVisiteur(String.format("NAISSANCE : Un bébé %s nommé %s est né !",
            bebe.getClass().getSimpleName(), bebe.getNom()));
        parent1.seReproduire(parent2);
        parent2.seReproduire(parent1);
    }

    private String demanderNomBebe(String especeAnimal) {
        return "Bébé" + especeAnimal + (animaux.size() + 1);
    }

    public void passerJournee() {
        jour++;
        gererEvenementsQuotidiens();
        verifierReproductions();
        gererEmployes();
        mettreAJourSatisfactionVisiteurs();
        gererNouveauxVisiteurs();
        statistiques.incrementerJour();
        nourrirAnimaux();
    }

    private void gererEvenementsQuotidiens() {
        animaux.forEach(animal -> {
            if (random.nextDouble() < 0.1) {
                animal.setEtatDeSante("Malade");
            }
            animal.incrementerJoursDepuisReproduction();
            animal.incrementerJoursPassesDansEnclos();
        });
    }

    private void gererEmployes() {
        Iterator<Employe> employeIterator = employes.iterator();
        while (employeIterator.hasNext()) {
            Employe employe = employeIterator.next();
            if (employe.estEnRepos()) {
                employe.diminuerJoursDeRepos();
            } else {
                employe.travaillerJournee();
                employe.effectuerTache();
                if (employe.estTropFatigue()) {
                    employeIterator.remove();
                    statistiques.ajouterIncident();
                }
            }
        }
    }

    private void gererNouveauxVisiteurs() {
        int nombreNouveauxVisiteurs = calculerNombreVisiteurs();
        for (int i = 0; i < nombreNouveauxVisiteurs; i++) {
            ajouterNouveauVisiteur();
        }
    }

    private void ajouterNouveauVisiteur() {
        String nomVisiteur = "Visiteur" + (visiteurs.size() + 1);
        int ageVisiteur = random.nextInt(70) + 5;
        Visiteur nouveauVisiteur = new Visiteur(nomVisiteur, ageVisiteur);
        nouveauVisiteur.acheterBillet();
        
        double prixBillet = nouveauVisiteur.calculerPrixBillet();
        budget += prixBillet;
        statistiques.ajouterRecette(prixBillet);
        
        if (!animaux.isEmpty()) {
            faireObserverAnimaux(nouveauVisiteur);
        }
        
        visiteurs.add(nouveauVisiteur);
        statistiques.ajouterVisiteur();
    }

    private void faireObserverAnimaux(Visiteur visiteur) {
        int nombreObservations = random.nextInt(3) + 1;
        for (int j = 0; j < nombreObservations; j++) {
            Animal animalObserve = animaux.get(random.nextInt(animaux.size()));
            visiteur.observerAnimal(animalObserve);
            ajouterActiviteVisiteur(visiteur.getNom() + " observe " + animalObserve.getNom());
            ajouterInteractionAnimale(visiteur.getNom(), animalObserve);
        }
    }

    public void nourrirAnimaux() {
        animaux.forEach(Animal::manger);
    }

    // Getters
    public List<Animal> getAnimaux() { return animaux; }
    public List<Habitat> getHabitats() { return habitats; }
    public List<Employe> getEmployes() { return employes; }
    public List<Visiteur> getVisiteurs() { return visiteurs; }
    public StatistiquesReserve getStatistiques() { return statistiques; }
    public List<String> getActivitesVisiteurs() { return activitesVisiteurs; }
    public int getJour() { return jour; }
    public double getBudget() { return budget; }

    // Méthodes d'ajout
    public boolean ajouterAnimal(Animal animal) { return animaux.add(animal); }

    public boolean ajouterHabitat(Habitat habitat) {
        if (budget >= habitat.getPrix() && habitats.add(habitat)) {
            budget -= habitat.getPrix();
            return true;
        }
        return false;
    }

    public boolean ajouterEmploye(Employe employe) { return employes.add(employe); }
    public void ajouterVisiteur(Visiteur visiteur) { visiteurs.add(visiteur); }
    public void ajouterActiviteVisiteur(String activite) { activitesVisiteurs.add(activite); }
} 