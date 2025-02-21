package com.example.evaluation_wallois_arthur.interfaces;

import com.example.evaluation_wallois_arthur.models.Animal;
import java.util.List;

/**
 * Interface définissant les règles de cohabitation entre les animaux
 * @author Arthur Wallois
 */
public interface Cohabitable {
    /**
     * Vérifie si l'animal peut cohabiter avec un autre animal
     * @param autre L'animal avec lequel on veut vérifier la cohabitation
     * @return true si la cohabitation est possible, false sinon
     */
    boolean peutCohabiterAvec(Animal autre);

    /**
     * Retourne la liste des espèces compatibles pour la cohabitation
     * @return Liste des noms d'espèces compatibles
     */
    List<String> getEspecesCompatibles();

    /**
     * Gère un conflit entre deux animaux
     * @param autre L'animal avec lequel il y a un conflit
     * @return true si le conflit a été résolu, false sinon
     */
    boolean gererConflit(Animal autre);

    /**
     * Calcule le niveau de stress de l'animal en fonction de ses cohabitants
     * @param cohabitants Liste des animaux présents dans le même habitat
     * @return Niveau de stress entre 0 (calme) et 100 (très stressé)
     */
    double calculerNiveauStress(List<Animal> cohabitants);
} 