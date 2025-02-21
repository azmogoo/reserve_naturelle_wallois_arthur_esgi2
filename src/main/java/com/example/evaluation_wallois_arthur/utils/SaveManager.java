package com.example.evaluation_wallois_arthur.utils;

import com.example.evaluation_wallois_arthur.models.animaux.*;
import com.example.evaluation_wallois_arthur.models.employes.*;
import com.example.evaluation_wallois_arthur.models.habitats.*;
import com.example.evaluation_wallois_arthur.simulation.Simulation;
import com.example.evaluation_wallois_arthur.models.Animal;
import com.example.evaluation_wallois_arthur.models.habitats.Habitat;
import com.example.evaluation_wallois_arthur.models.employes.Employe;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.databind.DeserializationFeature;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

public class SaveManager {
    private static final String SAVE_DIRECTORY = "saves";
    private static final String SAVE_FILE = "reserve_data.json";
    private final ObjectMapper mapper;

    public SaveManager() {
        // Création du dossier de sauvegarde s'il n'existe pas
        new File(SAVE_DIRECTORY).mkdirs();
        
        // Configuration de l'ObjectMapper
        this.mapper = JsonMapper.builder()
            .enable(SerializationFeature.INDENT_OUTPUT)
            .enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
            .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
            .build();
        
        // Activation du typage par défaut pour la gestion des classes polymorphiques
        mapper.activateDefaultTyping(mapper.getPolymorphicTypeValidator());
    }

    public void sauvegarderSimulation(Simulation simulation) {
        try {
            File saveFile = Paths.get(SAVE_DIRECTORY, SAVE_FILE).toFile();
            mapper.writeValue(saveFile, simulation);
            System.out.println("Simulation sauvegardée avec succès dans " + saveFile.getAbsolutePath());
        } catch (IOException e) {
            System.err.println("Erreur lors de la sauvegarde : " + e.getMessage());
            e.printStackTrace();
        }
    }

    public Simulation chargerSimulation() {
        try {
            File saveFile = Paths.get(SAVE_DIRECTORY, SAVE_FILE).toFile();
            if (saveFile.exists()) {
                Simulation simulation = mapper.readValue(saveFile, Simulation.class);
                System.out.println("Simulation chargée avec succès depuis " + saveFile.getAbsolutePath());
                return simulation;
            } else {
                System.out.println("Aucune sauvegarde trouvée, création d'une nouvelle simulation");
            }
        } catch (IOException e) {
            System.err.println("Erreur lors du chargement : " + e.getMessage());
            e.printStackTrace();
        }
        return new Simulation();
    }
} 