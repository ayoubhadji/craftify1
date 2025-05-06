package tn.esprit.entities;

import java.time.LocalDate;
import java.util.List;

public class Aventurier {
    private int id;
    private String nom;
    private String prenom;
    private String email;
    private LocalDate dateInscription;
    private String status;
    private String phoneNumber;
    private List<Expedition> expeditions;

    public Aventurier() {}

    public Aventurier(int id, String nom, String prenom, String email, LocalDate dateInscription, String status, String phoneNumber) {
        this.id = id;
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
        this.dateInscription = dateInscription;
        this.status = status;
        this.phoneNumber = phoneNumber;
    }

    public Aventurier(String nom, String prenom, String email, LocalDate dateInscription, String status, String phoneNumber) {
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
        this.dateInscription = dateInscription;
        this.status = status;
        this.phoneNumber = phoneNumber;
    }

    // ✅ Constructeur simplifié pour les listes (id et nom uniquement)
    public Aventurier(int id, String nom) {
        this.id = id;
        this.nom = nom;
        this.prenom = ""; // facultatif : à adapter selon ton affichage
    }

    // Getters et Setters

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

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDate getDateInscription() {
        return dateInscription;
    }

    public void setDateInscription(LocalDate dateInscription) {
        this.dateInscription = dateInscription;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    @Override
    public String toString() {
        return nom + " " + prenom + " (ID: " + id + ")";
    }
}
