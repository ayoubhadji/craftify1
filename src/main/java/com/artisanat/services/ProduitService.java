package com.artisanat.services;

import com.artisanat.entities.Produit;
import java.util.List;

public interface ProduitService {
    // Ajouter un produit à la base de données et retourner le produit ajouté
    Produit ajouterProduit(Produit produit);

    // Modifier un produit existant et retourner le produit modifié
    Produit modifierProduit(Produit produit);

    // Supprimer un produit de la base de données par son ID
    void supprimerProduit(int id);

    // Récupérer un produit spécifique par son ID
    Produit getProduitById(int id);

    // Récupérer tous les produits
    List<Produit> getAllProduits();

    // Récupérer les produits dont le stock est supérieur ou égal à stockMin
    List<Produit> getProduitsByStock(int stockMin);

    // Récupérer les produits dont le prix est inférieur ou égal à prixMax
    List<Produit> getProduitsByPrix(double prixMax);
}
