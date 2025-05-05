package entity;

import java.time.LocalDateTime;

public class Evenement {
    private int id;
    private String nom;
    private String description;
    private LocalDateTime dateDebut;
    private LocalDateTime dateFin;
    private String lieu;
    private int capacite;
    private String typeEvenement;
    private double prix;
    private String organisateur;
    private String image;
    private LocalDateTime dateCreation;

    public Evenement() {
    }

    public Evenement(String nom, String description, LocalDateTime dateDebut, LocalDateTime dateFin, 
                    String lieu, int capacite, String typeEvenement, double prix, 
                    String organisateur, String image, LocalDateTime dateCreation) {
        this.nom = nom;
        this.description = description;
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
        this.lieu = lieu;
        this.capacite = capacite;
        this.typeEvenement = typeEvenement;
        this.prix = prix;
        this.organisateur = organisateur;
        this.image = image;
        this.dateCreation = dateCreation;
    }

    public Evenement(int id, String nom, String description, LocalDateTime dateDebut, LocalDateTime dateFin, 
                    String lieu, int capacite, String typeEvenement, double prix, 
                    String organisateur, String image, LocalDateTime dateCreation) {
        this.id = id;
        this.nom = nom;
        this.description = description;
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
        this.lieu = lieu;
        this.capacite = capacite;
        this.typeEvenement = typeEvenement;
        this.prix = prix;
        this.organisateur = organisateur;
        this.image = image;
        this.dateCreation = dateCreation;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getDateDebut() {
        return dateDebut;
    }

    public void setDateDebut(LocalDateTime dateDebut) {
        this.dateDebut = dateDebut;
    }

    public LocalDateTime getDateFin() {
        return dateFin;
    }

    public void setDateFin(LocalDateTime dateFin) {
        this.dateFin = dateFin;
    }

    public String getLieu() {
        return lieu;
    }

    public void setLieu(String lieu) {
        this.lieu = lieu;
    }

    public int getCapacite() {
        return capacite;
    }

    public void setCapacite(int capacite) {
        this.capacite = capacite;
    }

    public String getTypeEvenement() {
        return typeEvenement;
    }

    public void setTypeEvenement(String typeEvenement) {
        this.typeEvenement = typeEvenement;
    }

    public double getPrix() {
        return prix;
    }

    public void setPrix(double prix) {
        this.prix = prix;
    }

    public String getOrganisateur() {
        return organisateur;
    }

    public void setOrganisateur(String organisateur) {
        this.organisateur = organisateur;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public LocalDateTime getDateCreation() {
        return dateCreation;
    }

    public void setDateCreation(LocalDateTime dateCreation) {
        this.dateCreation = dateCreation;
    }

    @Override
    public String toString() {
        return "Evenement{" +
                "id=" + id +
                ", nom='" + nom + '\'' +
                ", description='" + description + '\'' +
                ", dateDebut=" + dateDebut +
                ", dateFin=" + dateFin +
                ", lieu='" + lieu + '\'' +
                ", capacite=" + capacite +
                ", typeEvenement='" + typeEvenement + '\'' +
                ", prix=" + prix +
                ", organisateur='" + organisateur + '\'' +
                ", image='" + image + '\'' +
                ", dateCreation=" + dateCreation +
                '}';
    }
} 