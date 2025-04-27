package services.produit;

import entity.Produit;
import utils.DataSource;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ProduitServiceImpl implements ProduitService {
    private Connection conn;
    private PreparedStatement pst;

    public ProduitServiceImpl() {
        conn = DataSource.getInstance().getCnx();
    }

    @Override
    public List<Produit> getAllProduits() {
        List<Produit> produits = new ArrayList<>();
        String req = "SELECT * FROM produit";
        try {
            pst = conn.prepareStatement(req);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                Produit produit = new Produit();
                produit.setId(rs.getInt("id"));
                produit.setNom(rs.getString("nom"));
                produit.setDescription(rs.getString("description"));
                produit.setPrix(rs.getDouble("prix"));
                produit.setStock(rs.getInt("stock"));
                produit.setImageUrl(rs.getString("img_url"));
                produit.setArtisanId(rs.getInt("artisan_id"));
                Timestamp updatedAt = rs.getTimestamp("updated_at");
                if (updatedAt != null) {
                    produit.setDateModification(updatedAt.toLocalDateTime());
                }
                produits.add(produit);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return produits;
    }

    @Override
    public Produit getProduitById(int id) {
        String req = "SELECT * FROM produit WHERE id = ?";
        try {
            pst = conn.prepareStatement(req);
            pst.setInt(1, id);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                Produit produit = new Produit();
                produit.setId(rs.getInt("id"));
                produit.setNom(rs.getString("nom"));
                produit.setDescription(rs.getString("description"));
                produit.setPrix(rs.getDouble("prix"));
                produit.setStock(rs.getInt("stock"));
                produit.setImageUrl(rs.getString("img_url"));
                produit.setArtisanId(rs.getInt("artisan_id"));
                Timestamp updatedAt = rs.getTimestamp("updated_at");
                if (updatedAt != null) {
                    produit.setDateModification(updatedAt.toLocalDateTime());
                }
                return produit;
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    @Override
    public void addProduit(Produit produit) {
        String req = "INSERT INTO produit (nom, description, prix, stock, img_url, artisan_id, updated_at) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try {
            pst = conn.prepareStatement(req);
            pst.setString(1, produit.getNom());
            pst.setString(2, produit.getDescription());
            pst.setDouble(3, produit.getPrix());
            pst.setInt(4, produit.getStock());
            pst.setString(5, produit.getImageUrl());
            pst.setInt(6, produit.getArtisanId());
            pst.setTimestamp(7, Timestamp.valueOf(LocalDateTime.now()));
            pst.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void updateProduit(Produit produit) {
        String req = "UPDATE produit SET nom = ?, description = ?, prix = ?, stock = ?, img_url = ?, artisan_id = ?, updated_at = ? WHERE id = ?";
        try {
            pst = conn.prepareStatement(req);
            pst.setString(1, produit.getNom());
            pst.setString(2, produit.getDescription());
            pst.setDouble(3, produit.getPrix());
            pst.setInt(4, produit.getStock());
            pst.setString(5, produit.getImageUrl());
            pst.setInt(6, produit.getArtisanId());
            pst.setTimestamp(7, Timestamp.valueOf(LocalDateTime.now()));
            pst.setInt(8, produit.getId());
            pst.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void deleteProduit(int id) {
        String req = "DELETE FROM produit WHERE id = ?";
        try {
            pst = conn.prepareStatement(req);
            pst.setInt(1, id);
            pst.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}
