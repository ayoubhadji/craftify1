package tn.esprit.entities;

public class Quiz {

    private int id;
    private String nom;
    private int dureeMax; // representing duree_max
    private int expeditionId; // representing expedition_id

    // Constructor without parameters
    public Quiz() {
    }

    // Constructor with parameters
    public Quiz(int id, String nom, int dureeMax, int expeditionId) {
        this.id = id;
        this.nom = nom;
        this.dureeMax = dureeMax;
        this.expeditionId = expeditionId;
    }

    // Getters and setters
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

    public int getDureeMax() {
        return dureeMax;
    }

    public void setDureeMax(int dureeMax) {
        this.dureeMax = dureeMax;
    }

    public int getExpeditionId() {
        return expeditionId;
    }

    public void setExpeditionId(int expeditionId) {
        this.expeditionId = expeditionId;
    }

    @Override
    public String toString() {
        return "Quiz{" +
                "id=" + id +
                ", nom='" + nom + '\'' +
                ", dureeMax=" + dureeMax +
                ", expeditionId=" + expeditionId +
                '}';
    }
}
