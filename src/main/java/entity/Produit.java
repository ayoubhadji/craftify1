package entity;

import javafx.beans.property.*;

import java.time.LocalDateTime;

public class Produit {
    private final IntegerProperty id = new SimpleIntegerProperty();
    private final StringProperty nom = new SimpleStringProperty();
    private final StringProperty description = new SimpleStringProperty();
    private final DoubleProperty prix = new SimpleDoubleProperty();
    private final IntegerProperty stock = new SimpleIntegerProperty();
    private final StringProperty imageUrl = new SimpleStringProperty();
    private final IntegerProperty artisanId = new SimpleIntegerProperty();
    private final ObjectProperty<LocalDateTime> dateModification = new SimpleObjectProperty<>();

    public Produit() {
    }

    public Produit(String nom, String description, double prix, int stock, String imageUrl, int artisanId) {
        this.nom.set(nom);
        this.description.set(description);
        this.prix.set(prix);
        this.stock.set(stock);
        this.imageUrl.set(imageUrl);
        this.artisanId.set(artisanId);
        this.dateModification.set(LocalDateTime.now());
    }

    // Getters
    public int getId() {
        return id.get();
    }

    public String getNom() {
        return nom.get();
    }

    public String getDescription() {
        return description.get();
    }

    public double getPrix() {
        return prix.get();
    }

    public int getStock() {
        return stock.get();
    }

    public String getImageUrl() {
        return imageUrl.get();
    }

    public int getArtisanId() {
        return artisanId.get();
    }


    // Setters
    public void setId(int id) {
        this.id.set(id);
    }

    public void setNom(String nom) {
        this.nom.set(nom);
    }

    public void setDescription(String description) {
        this.description.set(description);
    }

    public void setPrix(double prix) {
        this.prix.set(prix);
    }

    public void setStock(int stock) {
        this.stock.set(stock);
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl.set(imageUrl);
    }

    public void setArtisanId(int artisanId) {
        this.artisanId.set(artisanId);
    }

    public void setDateModification(LocalDateTime dateModification) {
        this.dateModification.set(dateModification);
    }

    @Override
    public String toString() {
        return "Produit{" +
                "id=" + id.get() +
                ", nom='" + nom.get() + '\'' +
                ", description='" + description.get() + '\'' +
                ", prix=" + prix.get() +
                ", stock=" + stock.get() +
                ", imageUrl='" + imageUrl.get() + '\'' +
                ", artisanId=" + artisanId.get() +
                ", dateModification=" + dateModification.get() +
                '}';
    }
} 