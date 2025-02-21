module com.example.evaluation_wallois_arthur {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires com.fasterxml.jackson.core;
    requires com.fasterxml.jackson.databind;
    requires com.fasterxml.jackson.annotation;

    opens com.example.evaluation_wallois_arthur to javafx.fxml;
    opens com.example.evaluation_wallois_arthur.controllers to javafx.fxml;
    opens com.example.evaluation_wallois_arthur.models to javafx.base, com.fasterxml.jackson.databind;
    opens com.example.evaluation_wallois_arthur.models.animaux to javafx.base, com.fasterxml.jackson.databind;
    opens com.example.evaluation_wallois_arthur.models.employes to javafx.base, com.fasterxml.jackson.databind;
    opens com.example.evaluation_wallois_arthur.models.habitats to javafx.base, com.fasterxml.jackson.databind;

    exports com.example.evaluation_wallois_arthur;
    exports com.example.evaluation_wallois_arthur.controllers;
    exports com.example.evaluation_wallois_arthur.models;
    exports com.example.evaluation_wallois_arthur.models.animaux;
    exports com.example.evaluation_wallois_arthur.models.employes;
    exports com.example.evaluation_wallois_arthur.models.habitats;
}