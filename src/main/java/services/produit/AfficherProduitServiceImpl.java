package services.produit;

import entity.Produit;
import utils.MyDataBase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AfficherProduitServiceImpl implements AfficherProduitService {
    
    @Override
    public List<Produit> getAllProduits() {
        List<Produit> produits = new ArrayList<>();
        String sql = "SELECT * FROM produit";

        try (Connection conn = MyDataBase.getInstance().getMyConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                produits.add(createProduitFromResultSet(rs));
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération de tous les produits : " + e.getMessage());
            e.printStackTrace();
        }
        return produits;
    }

    @Override
    public Produit getProduitById(int id) {
        String sql = "SELECT * FROM produit WHERE id = ?";

        try (Connection conn = MyDataBase.getInstance().getMyConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return createProduitFromResultSet(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération du produit par ID : " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Produit> getProduitsByStock(int stockMin) {
        List<Produit> produits = new ArrayList<>();
        String sql = "SELECT * FROM produit WHERE stock >= ?";

        try (Connection conn = MyDataBase.getInstance().getMyConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, stockMin);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    produits.add(createProduitFromResultSet(rs));
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
        String sql = "SELECT * FROM produit WHERE prix <= ?";

        try (Connection conn = MyDataBase.getInstance().getMyConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setDouble(1, prixMax);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    produits.add(createProduitFromResultSet(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des produits par prix maximum : " + e.getMessage());
            e.printStackTrace();
        }
        return produits;
    }

    private Produit createProduitFromResultSet(ResultSet rs) throws SQLException {
        Produit produit = new Produit();
        produit.setId(rs.getInt("id"));
        produit.setNom(rs.getString("nom"));
        produit.setDescription(rs.getString("description"));
        produit.setPrix(rs.getDouble("prix"));
        produit.setStock(rs.getInt("stock"));
        produit.setImageUrl(rs.getString("image_url")); // Correction du nom de la colonne
        produit.setArtisanId(rs.getInt("artisan_id"));
        Timestamp updatedAt = rs.getTimestamp("updated_at");
        if (updatedAt != null) {
            produit.setDateModification(updatedAt.toLocalDateTime());
        }
        return produit;
    }
} 