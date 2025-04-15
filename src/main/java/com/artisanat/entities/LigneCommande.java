package com.artisanat.entities;

import javafx.beans.property.*;

public class LigneCommande {
    private final IntegerProperty id = new SimpleIntegerProperty();
    private final IntegerProperty commandeId = new SimpleIntegerProperty();
    private final ObjectProperty<Produit> produit = new SimpleObjectProperty<>();
    private final IntegerProperty quantite = new SimpleIntegerProperty();
    private final DoubleProperty prixUnitaire = new SimpleDoubleProperty();
    private final DoubleProperty sousTotal = new SimpleDoubleProperty();

    public LigneCommande() {
        // Initialiser les valeurs par défaut
        this.quantite.set(1);
        this.prixUnitaire.set(0.0);
        this.sousTotal.set(0.0);

        // Ajouter des listeners pour le calcul automatique du sous-total
        quantite.addListener((obs, oldVal, newVal) -> calculerSousTotal());
        prixUnitaire.addListener((obs, oldVal, newVal) -> calculerSousTotal());
    }

    public LigneCommande(int id, int commandeId, Produit produit, int quantite) {
        this();
        if (produit == null) {
            throw new IllegalArgumentException("Le produit ne peut pas être nul");
        }
        if (quantite <= 0) {
            throw new IllegalArgumentException("La quantité doit être positive");
        }
        
        this.id.set(id);
        this.commandeId.set(commandeId);
        this.produit.set(produit);
        this.quantite.set(quantite);
        this.prixUnitaire.set(produit.getPrix());
        calculerSousTotal();
    }

    // Getters et Setters pour les propriétés
    public int getId() {
        return id.get();
    }

    public IntegerProperty idProperty() {
        return id;
    }

    public void setId(int id) {
        this.id.set(id);
    }

    public int getCommandeId() {
        return commandeId.get();
    }

    public IntegerProperty commandeIdProperty() {
        return commandeId;
    }

    public void setCommandeId(int commandeId) {
        this.commandeId.set(commandeId);
    }

    public Produit getProduit() {
        return produit.get();
    }

    public ObjectProperty<Produit> produitProperty() {
        return produit;
    }

    public void setProduit(Produit produit) {
        if (produit == null) {
            throw new IllegalArgumentException("Le produit ne peut pas être nul");
        }
        this.produit.set(produit);
        this.prixUnitaire.set(produit.getPrix());
    }

    public int getQuantite() {
        return quantite.get();
    }

    public IntegerProperty quantiteProperty() {
        return quantite;
    }

    public void setQuantite(int quantite) {
        if (quantite <= 0) {
            throw new IllegalArgumentException("La quantité doit être positive");
        }
        this.quantite.set(quantite);
    }

    public double getPrixUnitaire() {
        return prixUnitaire.get();
    }

    public DoubleProperty prixUnitaireProperty() {
        return prixUnitaire;
    }

    public void setPrixUnitaire(double prixUnitaire) {
        if (prixUnitaire < 0) {
            throw new IllegalArgumentException("Le prix unitaire ne peut pas être négatif");
        }
        this.prixUnitaire.set(prixUnitaire);
    }

    public double getSousTotal() {
        return sousTotal.get();
    }

    public DoubleProperty sousTotalProperty() {
        return sousTotal;
    }

    private void calculerSousTotal() {
        sousTotal.set(quantite.get() * prixUnitaire.get());
    }

    @Override
    public String toString() {
        return "LigneCommande{" +
                "id=" + id.get() +
                ", commandeId=" + commandeId.get() +
                ", produit=" + produit.get() +
                ", quantite=" + quantite.get() +
                ", prixUnitaire=" + prixUnitaire.get() +
                ", sousTotal=" + sousTotal.get() +
                '}';
    }
} 