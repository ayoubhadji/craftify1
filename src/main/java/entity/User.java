package entity;

import java.time.LocalDateTime;

public class User {
    private int id;
    private String nom;
    private String email;
    private String code;
    private String role;
    private String sexe;
    private LocalDateTime dateNaissance;
    private LocalDateTime dateJoin;
    private String tel;
    private String address;
    private String fiscal;
    private String resetToken;

    // Constructeur par défaut
    public User(String testUser, String mail, String number, String admin, String homme, String s, Object dateJoin, String id, String address, String fis123456, String token123) {
    }

    // Constructeur avec tous les champs sauf id (si AUTO_INCREMENT)
    public User(String nom, String email, String code, String role, String sexe,
                LocalDateTime dateNaissance, LocalDateTime dateJoin, String tel,
                String address, String fiscal, String resetToken) {
        this.nom = nom;
        this.email = email;
        this.code = code;
        this.role = role;
        this.sexe = sexe;
        this.dateNaissance = dateNaissance;
        this.dateJoin = dateJoin;
        this.tel = tel;
        this.address = address;
        this.fiscal = fiscal;
        this.resetToken = resetToken;
    }

    public User() {

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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getSexe() {
        return sexe;
    }

    public void setSexe(String sexe) {
        this.sexe = sexe;
    }

    public LocalDateTime getDateNaissance() {
        return dateNaissance;
    }

    public void setDateNaissance(LocalDateTime dateNaissance) {
        this.dateNaissance = dateNaissance;
    }

    public LocalDateTime getDateJoin() {
        return dateJoin;
    }

    public void setDateJoin(LocalDateTime dateJoin) {
        this.dateJoin = dateJoin;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getFiscal() {
        return fiscal;
    }

    public void setFiscal(String fiscal) {
        this.fiscal = fiscal;
    }

    public String getResetToken() {
        return resetToken;
    }

    public void setResetToken(String resetToken) {
        this.resetToken = resetToken;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", nom='" + nom + '\'' +
                ", email='" + email + '\'' +
                ", code='" + code + '\'' +
                ", role='" + role + '\'' +
                ", sexe='" + sexe + '\'' +
                ", dateNaissance=" + dateNaissance +
                ", dateJoin=" + dateJoin +
                ", tel='" + tel + '\'' +
                ", address='" + address + '\'' +
                ", fiscal='" + fiscal + '\'' +
                ", resetToken='" + resetToken + '\'' +
                '}';
    }
}
