package com.example.evaluation_wallois_arthur.controllers;

import com.example.evaluation_wallois_arthur.models.*;
import com.example.evaluation_wallois_arthur.models.animaux.*;
import com.example.evaluation_wallois_arthur.models.employes.*;
import com.example.evaluation_wallois_arthur.models.habitats.*;
import com.example.evaluation_wallois_arthur.simulation.Simulation;
import com.example.evaluation_wallois_arthur.utils.SaveManager;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.collections.*;
import javafx.geometry.Insets;
import java.util.Optional;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;
import javafx.scene.control.SelectionMode;
import java.util.List;
import javafx.event.ActionEvent;

public class MainController {
    @FXML private ListView<Animal> listeAnimaux;
    @FXML private ListView<Habitat> listeHabitats;
    @FXML private ListView<Employe> listeEmployes;
    @FXML private TextArea journalSimulation;
    @FXML private Label statusLabel;
    @FXML private Label labelBudget;
    @FXML private Label labelNombreVisiteurs;
    @FXML private Label labelSatisfactionMoyenne;
    @FXML private Label labelRevenusBilletterie;
    @FXML private TextArea activitesVisiteurs;

    private Simulation simulation;
    private ObservableList<Animal> animaux = FXCollections.observableArrayList();
    private ObservableList<Habitat> habitats = FXCollections.observableArrayList();
    private ObservableList<Employe> employes = FXCollections.observableArrayList();
    private SaveManager saveManager = new SaveManager();
    
    // Variables pour suivre l'état de sélection précédent
    private Animal lastSelectedAnimal = null;
    private Habitat lastSelectedHabitat = null;
    private Employe lastSelectedEmploye = null;

    @FXML
    public void initialize() {
        simulation = new Simulation();
        listeAnimaux.setItems(animaux);
        listeHabitats.setItems(habitats);
        listeEmployes.setItems(employes);
        
        // Mise à jour initiale du budget
        updateBudget();
        
        // Configuration du mode de sélection pour permettre la désélection
        listeAnimaux.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        listeHabitats.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        listeEmployes.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

        // Permettre la désélection en cliquant sur l'élément sélectionné
        listeAnimaux.setOnMouseClicked(event -> {
            Animal currentSelected = listeAnimaux.getSelectionModel().getSelectedItem();
            if (currentSelected != null && currentSelected == lastSelectedAnimal) {
                listeAnimaux.getSelectionModel().clearSelection();
                lastSelectedAnimal = null;
            } else {
                lastSelectedAnimal = currentSelected;
            }
        });

        listeHabitats.setOnMouseClicked(event -> {
            Habitat currentSelected = listeHabitats.getSelectionModel().getSelectedItem();
            if (currentSelected != null && currentSelected == lastSelectedHabitat) {
                listeHabitats.getSelectionModel().clearSelection();
                lastSelectedHabitat = null;
            } else {
                lastSelectedHabitat = currentSelected;
            }
        });

        listeEmployes.setOnMouseClicked(event -> {
            Employe currentSelected = listeEmployes.getSelectionModel().getSelectedItem();
            if (currentSelected != null && currentSelected == lastSelectedEmploye) {
                listeEmployes.getSelectionModel().clearSelection();
                lastSelectedEmploye = null;
            } else {
                lastSelectedEmploye = currentSelected;
            }
        });
        
        statusLabel.setText("Simulation prête");

        // Mise à jour des statistiques visiteurs toutes les 5 secondes
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(5), event -> {
            updateVisiteurStats();
            verifierAlertesSante();
        }));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();

        // Initialisation des statistiques visiteurs
        updateVisiteurStats();

        // Personnaliser l'affichage des employés
        listeEmployes.setCellFactory(lv -> new ListCell<Employe>() {
            @Override
            protected void updateItem(Employe employe, boolean empty) {
                super.updateItem(employe, empty);
                if (empty || employe == null) {
                    setText(null);
                    setStyle("");
                } else {
                    setText(employe.toString());
                    if (employe.estEnRepos()) {
                        setStyle("-fx-text-fill: gray; -fx-background-color: #f0f0f0;");
                        setDisable(true);
                    } else {
                        setStyle("");
                        setDisable(false);
                    }
                }
            }
        });
    }

    private void updateVisiteurStats() {
        if (simulation != null) {
            labelNombreVisiteurs.setText(String.format("Nombre de visiteurs : %d", simulation.getVisiteurs().size()));
            labelSatisfactionMoyenne.setText(String.format("Satisfaction moyenne : %.1f%%", simulation.getStatistiques().getSatisfactionVisiteurs()));
            labelRevenusBilletterie.setText(String.format("Revenus billetterie : %.2f€", simulation.getStatistiques().getRevenuTotal()));
            
            // Mise à jour des activités
            StringBuilder activites = new StringBuilder();
            for (String activite : simulation.getActivitesVisiteurs()) {
                activites.append(activite).append("\n");
            }
            activitesVisiteurs.setText(activites.toString());
        }
    }

    private void updateActivitesVisiteurs(String activite) {
        activitesVisiteurs.appendText(String.format("[Jour %d] %s\n", simulation.getJour(), activite));
    }

    @FXML
    private void ajouterHabitat() {
        Dialog<Habitat> dialog = new Dialog<>();
        dialog.setTitle("Ajouter un habitat");
        dialog.setHeaderText("Nouvel habitat");

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField nomField = new TextField();
        nomField.setPromptText("Nom");
        
        ComboBox<String> typeHabitat = new ComboBox<>();
        typeHabitat.getItems().addAll("Savane", "Volière", "Aquarium");
        typeHabitat.setValue("Savane");

        ComboBox<TailleHabitat> tailleHabitat = new ComboBox<>();
        tailleHabitat.getItems().addAll(TailleHabitat.values());
        tailleHabitat.setValue(TailleHabitat.PETIT);

        Label prixLabel = new Label();
        prixLabel.setText(String.format("Prix : %.2f€", TailleHabitat.PETIT.getPrix()));

        tailleHabitat.setOnAction(e -> {
            prixLabel.setText(String.format("Prix : %.2f€", tailleHabitat.getValue().getPrix()));
        });

        grid.add(new Label("Type:"), 0, 0);
        grid.add(typeHabitat, 1, 0);
        grid.add(new Label("Nom:"), 0, 1);
        grid.add(nomField, 1, 1);
        grid.add(new Label("Taille:"), 0, 2);
        grid.add(tailleHabitat, 1, 2);
        grid.add(prixLabel, 1, 3);

        dialog.getDialogPane().setContent(grid);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        dialog.setResultConverter(buttonType -> {
            if (buttonType == ButtonType.OK) {
                try {
                    String nom = nomField.getText();
                    TailleHabitat taille = tailleHabitat.getValue();
                    double prix = taille.getPrix();

                    if (simulation.getBudget() < prix) {
                        showAlert("Erreur", "Budget insuffisant pour créer cet habitat !");
                        return null;
                    }

                    Habitat nouvelHabitat;
                    switch (typeHabitat.getValue()) {
                        case "Savane":
                            nouvelHabitat = new Savane(nom, taille);
                            break;
                        case "Volière":
                            nouvelHabitat = new Voliere(nom, taille);
                            break;
                        case "Aquarium":
                            nouvelHabitat = new Aquarium(nom, taille);
                            break;
                        default:
                            nouvelHabitat = new Savane(nom, taille);
                    }
                    
                    simulation.ajouterHabitat(nouvelHabitat);
                    habitats.add(nouvelHabitat);
                    updateJournal("Nouvel habitat ajouté : " + nouvelHabitat.toString());
                    updateBudget();
                    return nouvelHabitat;
                } catch (Exception e) {
                    showAlert("Erreur", "Une erreur est survenue lors de la création de l'habitat.");
                    return null;
                }
            }
            return null;
        });

        dialog.showAndWait();
    }

    @FXML
    private void ajouterAnimal() {
        if (habitats.isEmpty()) {
            showAlert("Erreur", "Vous devez d'abord créer un habitat avant d'ajouter un animal !");
            return;
        }

        Dialog<Animal> dialog = new Dialog<>();
        dialog.setTitle("Ajouter un animal");
        dialog.setHeaderText("Nouvel animal");

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField nomField = new TextField();
        nomField.setPromptText("Nom");
        TextField ageField = new TextField();
        ageField.setPromptText("Age");
        TextField poidsField = new TextField();
        poidsField.setPromptText("Poids");
        TextField tailleField = new TextField();
        tailleField.setPromptText("Taille");
        
        ComboBox<String> typeAnimal = new ComboBox<>();
        typeAnimal.getItems().addAll("Lion", "Serpent", "Aigle");
        typeAnimal.setValue("Lion");

        ComboBox<Habitat> habitatCombo = new ComboBox<>();
        
        typeAnimal.setOnAction(e -> {
            habitatCombo.getItems().clear();
            String type = typeAnimal.getValue();
            for (Habitat habitat : habitats) {
                boolean compatible = false;
                switch (type) {
                    case "Lion":
                        compatible = habitat instanceof Savane;
                        break;
                    case "Serpent":
                        compatible = habitat instanceof Aquarium;
                        break;
                    case "Aigle":
                        compatible = habitat instanceof Voliere;
                        break;
                }
                if (compatible && habitat.peutAccueillirNouvelAnimal()) {
                    habitatCombo.getItems().add(habitat);
                }
            }
            if (habitatCombo.getItems().isEmpty()) {
                showAlert("Information", "Aucun habitat compatible disponible pour ce type d'animal.");
            } else {
                habitatCombo.setValue(habitatCombo.getItems().get(0));
            }
        });
        
        typeAnimal.fireEvent(new ActionEvent());

        grid.add(new Label("Type:"), 0, 0);
        grid.add(typeAnimal, 1, 0);
        grid.add(new Label("Nom:"), 0, 1);
        grid.add(nomField, 1, 1);
        grid.add(new Label("Age:"), 0, 2);
        grid.add(ageField, 1, 2);
        grid.add(new Label("Poids:"), 0, 3);
        grid.add(poidsField, 1, 3);
        grid.add(new Label("Taille:"), 0, 4);
        grid.add(tailleField, 1, 4);
        grid.add(new Label("Habitat:"), 0, 5);
        grid.add(habitatCombo, 1, 5);

        dialog.getDialogPane().setContent(grid);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        dialog.setResultConverter(buttonType -> {
            if (buttonType == ButtonType.OK) {
                try {
                    String nom = nomField.getText();
                    int age = Integer.parseInt(ageField.getText());
                    double poids = Double.parseDouble(poidsField.getText());
                    double taille = Double.parseDouble(tailleField.getText());
                    Habitat habitat = habitatCombo.getValue();

                    if (habitat == null) {
                        showAlert("Erreur", "Veuillez sélectionner un habitat compatible.");
                        return null;
                    }

                    if (!habitat.peutAccueillirNouvelAnimal()) {
                        showAlert("Erreur", "L'habitat sélectionné est plein.");
                        return null;
                    }

                    Animal nouvelAnimal;
                    switch (typeAnimal.getValue()) {
                        case "Lion":
                            nouvelAnimal = new Lion(nom, age, poids, taille);
                            break;
                        case "Serpent":
                            nouvelAnimal = new Serpent(nom, age, poids, taille);
                            break;
                        case "Aigle":
                            nouvelAnimal = new Aigle(nom, age, poids, taille);
                            break;
                        default:
                            nouvelAnimal = new Lion(nom, age, poids, taille);
                    }
                    
                    habitat.ajouterAnimal(nouvelAnimal);
                    animaux.add(nouvelAnimal);
                    simulation.ajouterAnimal(nouvelAnimal);
                    updateJournal("Nouvel animal ajouté : " + nouvelAnimal.toString() + " dans " + habitat.getNom());
                    return nouvelAnimal;
                } catch (NumberFormatException e) {
                    showAlert("Erreur", "Veuillez entrer des valeurs numériques valides.");
                    return null;
                }
            }
            return null;
        });

        dialog.showAndWait();
    }

    @FXML
    private void ajouterEmploye() {
        Dialog<Employe> dialog = new Dialog<>();
        dialog.setTitle("Ajouter un employé");
        dialog.setHeaderText("Nouvel employé");

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField nomField = new TextField();
        nomField.setPromptText("Nom");
        
        ComboBox<String> typeEmploye = new ComboBox<>();
        typeEmploye.getItems().addAll("Soigneur", "Vétérinaire");
        typeEmploye.setValue("Soigneur");

        // Afficher le salaire fixe
        Label salaireLabel = new Label();
        typeEmploye.setOnAction(e -> {
            double salaire = typeEmploye.getValue().equals("Soigneur") ? 2500.0 : 5000.0;
            salaireLabel.setText(String.format("Salaire : %.2f€", salaire));
        });
        salaireLabel.setText("Salaire : 2500.00€"); // Valeur initiale pour Soigneur

        grid.add(new Label("Type:"), 0, 0);
        grid.add(typeEmploye, 1, 0);
        grid.add(new Label("Nom:"), 0, 1);
        grid.add(nomField, 1, 1);
        grid.add(salaireLabel, 1, 2);

        dialog.getDialogPane().setContent(grid);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        dialog.setResultConverter(buttonType -> {
            if (buttonType == ButtonType.OK) {
                String nom = nomField.getText();
                if (nom == null || nom.trim().isEmpty()) {
                    showAlert("Erreur", "Veuillez entrer un nom valide.");
                    return null;
                }

                Employe nouvelEmploye;
                if (typeEmploye.getValue().equals("Soigneur")) {
                    nouvelEmploye = new SoigneurImpl(nom);
                } else {
                    nouvelEmploye = new Veterinaire(nom);
                }

                if (simulation.ajouterEmploye(nouvelEmploye)) {
                    employes.add(nouvelEmploye);
                    updateJournal("Nouvel employé ajouté : " + nom);
                    return nouvelEmploye;
                } else {
                    showAlert("Erreur", "Budget insuffisant pour embaucher cet employé !");
                    return null;
                }
            }
            return null;
        });

        dialog.showAndWait();
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private void updateJournal(String message) {
        journalSimulation.appendText(message + "\n");
    }

    @FXML
    private void nourrirAnimaux() {
        if (employes.isEmpty()) {
            showAlert("Erreur", "Aucun employé disponible pour nourrir les animaux !");
            return;
        }

        boolean soigneurDisponible = false;
        for (Employe employe : employes) {
            if (employe instanceof SoigneurImpl && !employe.estEnRepos()) {
                soigneurDisponible = true;
                break;
            }
        }

        if (!soigneurDisponible) {
            showAlert("Erreur", "Aucun soigneur disponible pour nourrir les animaux !");
            return;
        }

        simulation.nourrirAnimaux();
        updateJournal("Les animaux ont été nourris");
        listeAnimaux.refresh();
    }

    @FXML
    private void faireReposerEmploye() {
        Employe selected = listeEmployes.getSelectionModel().getSelectedItem();
        if (selected != null) {
            if (selected.estEnRepos()) {
                showAlert("Erreur", "Cet employé est déjà en repos !");
                return;
            }

            // Création de la boîte de dialogue pour choisir le nombre de jours
            Dialog<Integer> dialog = new Dialog<>();
            dialog.setTitle("Période de repos");
            dialog.setHeaderText("Choisir le nombre de jours de repos pour " + selected.getNom());

            // Création du slider pour choisir le nombre de jours (1-7)
            Slider slider = new Slider(1, 7, 1);
            slider.setShowTickLabels(true);
            slider.setShowTickMarks(true);
            slider.setMajorTickUnit(1);
            slider.setMinorTickCount(0);
            slider.setBlockIncrement(1);
            slider.setSnapToTicks(true);

            // Label pour afficher la valeur actuelle
            Label valueLabel = new Label("1 jour");
            slider.valueProperty().addListener((obs, oldVal, newVal) -> {
                int days = newVal.intValue();
                valueLabel.setText(days + (days > 1 ? " jours" : " jour"));
            });

            VBox content = new VBox(10);
            content.getChildren().addAll(new Label("Nombre de jours :"), slider, valueLabel);
            dialog.getDialogPane().setContent(content);
            dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

            dialog.setResultConverter(buttonType -> {
                if (buttonType == ButtonType.OK) {
                    return (int) slider.getValue();
                }
                return null;
            });

            Optional<Integer> result = dialog.showAndWait();
            result.ifPresent(nombreJours -> {
                selected.setEnRepos(true);
                selected.setJoursDeReposRestants(nombreJours);
                updateJournal(selected.getNom() + " prend " + nombreJours + " jour(s) de repos");
                listeEmployes.refresh();
            });
        } else {
            showAlert("Attention", "Veuillez sélectionner un employé à mettre au repos.");
        }
    }

    @FXML
    private void soignerAnimal() {
        Animal selected = listeAnimaux.getSelectionModel().getSelectedItem();
        if (selected != null) {
            // Vérifier si un vétérinaire non en repos est disponible
            boolean veterinaireTrouve = false;
            for (Employe employe : employes) {
                if (employe instanceof Veterinaire && !employe.estEnRepos()) {
                    veterinaireTrouve = true;
                    ((Veterinaire) employe).soigner(selected);
                    updateJournal(selected.getNom() + " a été soigné par " + employe.getNom());
                    listeAnimaux.refresh();
                    break;
                }
            }
            
            if (!veterinaireTrouve) {
                showAlert("Erreur", "Aucun vétérinaire disponible pour soigner l'animal !");
            }
        } else {
            showAlert("Attention", "Veuillez sélectionner un animal à soigner.");
        }
    }

    @FXML
    private void nettoyerHabitat() {
        Habitat selected = listeHabitats.getSelectionModel().getSelectedItem();
        if (selected != null) {
            selected.nettoyer();
            updateJournal("L'habitat " + selected.getNom() + " a été nettoyé");
        } else {
            showAlert("Attention", "Veuillez sélectionner un habitat à nettoyer.");
        }
    }

    @FXML
    private void passerJournee() {
        // Désélectionner tous les éléments avant de passer la journée
        listeAnimaux.getSelectionModel().clearSelection();
        listeHabitats.getSelectionModel().clearSelection();
        listeEmployes.getSelectionModel().clearSelection();

        simulation.passerJournee();
        updateJournal("\n=== Nouvelle journée ===");
        
        // Mise à jour du budget
        updateBudget();
        
        // Mise à jour des statistiques visiteurs
        updateVisiteurStats();
        
        // Afficher les activités des visiteurs
        List<String> activites = simulation.getActivitesVisiteurs();
        for (String activite : activites) {
            updateActivitesVisiteurs(activite);
        }
        
        // Rafraîchir les listes
        animaux.clear();
        animaux.addAll(simulation.getAnimaux());
        habitats.clear();
        habitats.addAll(simulation.getHabitats());
        employes.clear();
        employes.addAll(simulation.getEmployes());
        
        listeAnimaux.refresh();
        listeHabitats.refresh();
        listeEmployes.refresh();
    }

    @FXML
    private void licencierEmploye() {
        Employe selected = listeEmployes.getSelectionModel().getSelectedItem();
        if (selected != null) {
            Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
            confirmation.setTitle("Confirmation de licenciement");
            confirmation.setHeaderText("Licenciement de " + selected.getNom());
            confirmation.setContentText("Êtes-vous sûr de vouloir licencier cet employé ?");

            Optional<ButtonType> result = confirmation.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                employes.remove(selected);
                simulation.getEmployes().remove(selected);
                updateJournal(selected.getNom() + " a été licencié");
                statusLabel.setText("Employé licencié");
            }
        } else {
            showAlert("Attention", "Veuillez sélectionner un employé à licencier.");
        }
    }

    @FXML
    private void sauvegarderSimulation() {
        saveManager.sauvegarderSimulation(simulation);
        updateJournal("Simulation sauvegardée");
        statusLabel.setText("Sauvegarde effectuée");
    }

    @FXML
    private void chargerSimulation() {
        simulation = saveManager.chargerSimulation();
        
        // Mise à jour des listes
        animaux.clear();
        animaux.addAll(simulation.getAnimaux());
        
        habitats.clear();
        habitats.addAll(simulation.getHabitats());
        
        employes.clear();
        employes.addAll(simulation.getEmployes());
        
        updateJournal("Simulation chargée");
        statusLabel.setText("Chargement effectué");
    }

    private void verifierAlertesSante() {
        if (simulation != null && !simulation.getAnimaux().isEmpty()) {
            for (Animal animal : simulation.getAnimaux()) {
                if (!"Bon".equals(animal.getEtatDeSante()) && !"Excellent".equals(animal.getEtatDeSante())) {
                    updateJournal("ALERTE SANTÉ: " + animal.getNom() + " est " + animal.getEtatDeSante().toLowerCase());
                }
            }
        }
    }

    private void updateBudget() {
        labelBudget.setText(String.format("%.2f€", simulation.getBudget()));
    }

    public String demanderNomBebe(String especeAnimal) {
        TextInputDialog dialog = new TextInputDialog("Bébé" + especeAnimal);
        dialog.setTitle("Nouvelle naissance !");
        dialog.setHeaderText("Un bébé " + especeAnimal + " vient de naître !");
        dialog.setContentText("Choisissez un nom pour le bébé :");

        // Style personnalisé pour la boîte de dialogue
        DialogPane dialogPane = dialog.getDialogPane();
        dialogPane.getStylesheets().add(
            getClass().getResource("/com/example/evaluation_wallois_arthur/styles/dialog.css").toExternalForm()
        );
        dialogPane.getStyleClass().add("custom-dialog");

        Optional<String> result = dialog.showAndWait();
        return result.orElse("Bébé" + especeAnimal);
    }
} 