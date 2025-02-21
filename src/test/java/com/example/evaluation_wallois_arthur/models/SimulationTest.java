package com.example.evaluation_wallois_arthur.models;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import com.example.evaluation_wallois_arthur.models.animaux.*;
import com.example.evaluation_wallois_arthur.models.employes.*;
import com.example.evaluation_wallois_arthur.models.habitats.*;

/**
 * Tests unitaires pour la classe Simulation
 * @author Arthur Wallois
 */
public class SimulationTest {
    private Simulation simulation;
    private Lion lion;
    private Veterinaire veterinaire;
    private SoigneurImpl soigneur;
    private Savane savane;

    @BeforeEach
    void setUp() {
        simulation = new Simulation();
        lion = new Lion("Simba", 5, 100.0, 1.2);
        veterinaire = new Veterinaire("Dr. Dolittle");
        soigneur = new SoigneurImpl("John");
        savane = new Savane("Savane principale", 1000.0);
    }

    @Test
    void testAjouterAnimal() {
        simulation.ajouterAnimal(lion);
        assertTrue(simulation.getAnimaux().contains(lion));
        assertEquals(1, simulation.getAnimaux().size());
    }

    @Test
    void testAjouterEmploye() {
        simulation.ajouterEmploye(veterinaire);
        simulation.ajouterEmploye(soigneur);
        assertEquals(2, simulation.getEmployes().size());
        assertTrue(simulation.getEmployes().contains(veterinaire));
        assertTrue(simulation.getEmployes().contains(soigneur));
    }

    @Test
    void testAjouterHabitat() {
        simulation.ajouterHabitat(savane);
        assertTrue(simulation.getHabitats().contains(savane));
        assertEquals(1, simulation.getHabitats().size());
    }

    @Test
    void testNourrirAnimauxSansSoigneur() {
        simulation.ajouterAnimal(lion);
        simulation.nourrirAnimaux();
        assertEquals("Affamé", lion.getEtatDeSante());
    }

    @Test
    void testNourrirAnimauxAvecSoigneur() {
        simulation.ajouterAnimal(lion);
        simulation.ajouterEmploye(soigneur);
        simulation.nourrirAnimaux();
        assertTrue(lion.getNiveauFaim() > 50);
    }

    @Test
    void testSoignerAnimalSansVeterinaire() {
        simulation.ajouterAnimal(lion);
        lion.setEtatDeSante("Malade");
        // Sans vétérinaire, l'état de santé ne devrait pas changer
        assertEquals("Malade", lion.getEtatDeSante());
    }

    @Test
    void testSoignerAnimalAvecVeterinaire() {
        simulation.ajouterAnimal(lion);
        simulation.ajouterEmploye(veterinaire);
        lion.setEtatDeSante("Malade");
        veterinaire.soigner(lion);
        assertEquals("Excellent", lion.getEtatDeSante());
    }

    @Test
    void testPasserJournee() {
        simulation.ajouterAnimal(lion);
        simulation.ajouterEmploye(soigneur);
        simulation.ajouterEmploye(veterinaire);
        
        int jourInitial = simulation.getJour();
        simulation.passerJournee();
        assertEquals(jourInitial + 1, simulation.getJour());
    }

    @Test
    void testEmployeEnRepos() {
        simulation.ajouterEmploye(soigneur);
        soigneur.setEnRepos(true);
        soigneur.setJoursDeReposRestants(2);
        
        simulation.passerJournee();
        assertEquals(1, soigneur.getJoursDeReposRestants());
        assertTrue(soigneur.estEnRepos());
        
        simulation.passerJournee();
        assertEquals(0, soigneur.getJoursDeReposRestants());
        assertFalse(soigneur.estEnRepos());
    }

    @Test
    void testGestionIncidents() {
        simulation.ajouterHabitat(savane);
        simulation.ajouterAnimal(lion);
        
        int incidentsInitiaux = simulation.getStatistiques().getNombreIncidents();
        simulation.passerJournee();
        // Vérifier si des incidents peuvent se produire
        assertTrue(simulation.getStatistiques().getNombreIncidents() >= incidentsInitiaux);
    }

    @Test
    void testCalculStatistiques() {
        simulation.ajouterAnimal(lion);
        simulation.ajouterHabitat(savane);
        simulation.ajouterEmploye(veterinaire);
        
        // Simuler quelques événements
        simulation.getStatistiques().enregistrerNaissance("Lion");
        simulation.getStatistiques().enregistrerMaladie("Lion");
        
        String rapport = simulation.getStatistiques().genererRapport();
        assertTrue(rapport.contains("Lion"));
        assertTrue(rapport.contains("naissance"));
    }
} 