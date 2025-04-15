package com.artisanat.entities;

import javafx.beans.property.*;
import java.time.LocalDateTime;

public class Produit {
    private final IntegerProperty id = new SimpleIntegerProperty();
    private final StringProperty nom = new SimpleStringProperty();
    private final StringProperty description = new SimpleStringProperty();
    private final DoubleProperty prix = new SimpleDoubleProperty();
    private final IntegerProperty stock = new SimpleIntegerProperty();
    private final ObjectProperty<LocalDateTime> dateCreation = new SimpleObjectProperty<>();
    private final ObjectProperty<LocalDateTime> dateModification = new SimpleObjectProperty<>();
    private final StringProperty imageUrl = new SimpleStringProperty();

    public Produit() {
        this.dateCreation.set(LocalDateTime.now());
        this.dateModification.set(LocalDateTime.now());
    }

    public Produit(String nom, String description, double prix, int stock, String imageUrl) {
        this();
        this.nom.set(nom);
        this.description.set(description);
        this.prix.set(prix);
        this.stock.set(stock);
        this.imageUrl.set(imageUrl);
    }

    // Getters et setters pour les propriétés
    public int getId() {
        return id.get();
    }

    public IntegerProperty idProperty() {
        return id;
    }

    public void setId(int id) {
        this.id.set(id);
    }

    public String getNom() {
        return nom.get();
    }

    public StringProperty nomProperty() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom.set(nom);
    }

    public String getDescription() {
        return description.get();
    }

    public StringProperty descriptionProperty() {
        return description;
    }

    public void setDescription(String description) {
        this.description.set(description);
    }

    public double getPrix() {
        return prix.get();
    }

    public DoubleProperty prixProperty() {
        return prix;
    }

    public void setPrix(double prix) {
        this.prix.set(prix);
    }

    public int getStock() {
        return stock.get();
    }

    public IntegerProperty stockProperty() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock.set(stock);
    }

    public LocalDateTime getDateCreation() {
        return dateCreation.get();
    }

    public ObjectProperty<LocalDateTime> dateCreationProperty() {
        return dateCreation;
    }

    public void setDateCreation(LocalDateTime dateCreation) {
        this.dateCreation.set(dateCreation);
    }

    public LocalDateTime getDateModification() {
        return dateModification.get();
    }

    public ObjectProperty<LocalDateTime> dateModificationProperty() {
        return dateModification;
    }

    public void setDateModification(LocalDateTime dateModification) {
        this.dateModification.set(dateModification);
    }

    public String getImageUrl() {
        return imageUrl.get();
    }

    public StringProperty imageUrlProperty() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl.set(imageUrl);
    }

    @Override
    public String toString() {
        return "Produit{" +
                "id=" + id.get() +
                ", nom=" + nom.get() +
                ", description=" + description.get() +
                ", prix=" + prix.get() +
                ", stock=" + stock.get() +
                ", dateCreation=" + dateCreation.get() +
                ", dateModification=" + dateModification.get() +
                ", imageUrl=" + imageUrl.get() +
                '}';
    }
} 