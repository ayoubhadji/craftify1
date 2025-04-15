package com.artisanat.services.impl;

import com.artisanat.entities.Produit;
import com.artisanat.services.ProduitService;
import com.artisanat.database.MyDataBase;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ProduitServiceImpl implements ProduitService {

    public ProduitServiceImpl() {
        // Initialiser la base de données si nécessaire
        MyDataBase.initializeDatabase();
    }

    @Override
    public Produit ajouterProduit(Produit produit) {
        String sql = "INSERT INTO produits (nom, description, prix, stock, date_creation, date_modification, image_url) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = MyDataBase.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setString(1, produit.getNom());
            pstmt.setString(2, produit.getDescription());
            pstmt.setDouble(3, produit.getPrix());
            pstmt.setInt(4, produit.getStock());
            pstmt.setTimestamp(5, Timestamp.valueOf(produit.getDateCreation()));
            pstmt.setTimestamp(6, Timestamp.valueOf(produit.getDateModification()));
            pstmt.setString(7, produit.getImageUrl());

            pstmt.executeUpdate();

            try (ResultSet rs = pstmt.getGeneratedKeys()) {
                if (rs.next()) {
                    produit.setId(rs.getInt(1)); // Récupérer l'ID généré
                }
            }

            return produit;
        } catch (SQLException e) {
            System.err.println("Erreur lors de l'ajout du produit : " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public Produit modifierProduit(Produit produit) {
        String sql = "UPDATE produits SET nom = ?, description = ?, prix = ?, stock = ?, " +
                "date_modification = ?, image_url = ? WHERE id = ?";

        try (Connection conn = MyDataBase.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, produit.getNom());
            pstmt.setString(2, produit.getDescription());
            pstmt.setDouble(3, produit.getPrix());
            pstmt.setInt(4, produit.getStock());
            pstmt.setTimestamp(5, Timestamp.valueOf(LocalDateTime.now()));
            pstmt.setString(6, produit.getImageUrl());
            pstmt.setInt(7, produit.getId());

            int rowsUpdated = pstmt.executeUpdate();
            if (rowsUpdated > 0) {
                return produit; // Retourner le produit mis à jour
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la modification du produit : " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void supprimerProduit(int id) {
        String sql = "DELETE FROM produits WHERE id = ?";

        try (Connection conn = MyDataBase.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Erreur lors de la suppression du produit : " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public Produit getProduitById(int id) {
        String sql = "SELECT * FROM produits WHERE id = ?";

        try (Connection conn = MyDataBase.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return createProduitFromResultSet(rs); // Créer un produit depuis le ResultSet
                }
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération du produit par ID : " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Produit> getAllProduits() {
        List<Produit> produits = new ArrayList<>();
        String sql = "SELECT * FROM produits";

        try (Connection conn = MyDataBase.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                produits.add(createProduitFromResultSet(rs)); // Ajouter chaque produit trouvé à la liste
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération de tous les produits : " + e.getMessage());
            e.printStackTrace();
        }
        return produits;
    }

    @Override
    public List<Produit> getProduitsByStock(int stockMin) {
        List<Produit> produits = new ArrayList<>();
        String sql = "SELECT * FROM produits WHERE stock >= ?";

        try (Connection conn = MyDataBase.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, stockMin);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    produits.add(createProduitFromResultSet(rs)); // Ajouter chaque produit filtré à la liste
                }
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des produits par stock minimum : " + e.getMessage());
            e.printStackTrace();
        }
        return produits;
    }

    @Override
    public List<Produit> getProduitsByPrix(double prixMax) {
        List<Produit> produits = new ArrayList<>();
        String sql = "SELECT * FROM produits WHERE prix <= ?";

        try (Connection conn = MyDataBase.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setDouble(1, prixMax);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    produits.add(createProduitFromResultSet(rs)); // Ajouter chaque produit filtré à la liste
                }
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des produits par prix maximum : " + e.getMessage());
            e.printStackTrace();
        }
        return produits;
    }

    // Méthode utilitaire pour créer un produit à partir du ResultSet
    private Produit createProduitFromResultSet(ResultSet rs) throws SQLException {
        Produit produit = new Produit();
        produit.setId(rs.getInt("id"));
        produit.setNom(rs.getString("nom"));
        produit.setDescription(rs.getString("description"));
        produit.setPrix(rs.getDouble("prix"));
        produit.setStock(rs.getInt("stock"));
        produit.setDateCreation(rs.getTimestamp("date_creation").toLocalDateTime());
        produit.setDateModification(rs.getTimestamp("date_modification").toLocalDateTime());
        produit.setImageUrl(rs.getString("image_url"));
        return produit;
    }
}
