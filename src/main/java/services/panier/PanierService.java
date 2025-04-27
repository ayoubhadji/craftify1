package services.panier;

import entity.Panier;
import utils.MyDataBase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PanierService {
    private Connection conn;

    public PanierService() {
        conn = MyDataBase.getInstance().getMyConnection();
    }

    private boolean userExists(int userId) throws SQLException {
        String sql = "SELECT COUNT(*) FROM user WHERE id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        }
        return false;
    }

    private boolean produitExists(int produitId) throws SQLException {
        String sql = "SELECT COUNT(*) FROM produit WHERE id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, produitId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        }
        return false;
    }

    public void ajouterAuPanier(Panier panier) throws SQLException {
        // Vérifier si l'utilisateur et le produit existent
        if (!userExists(panier.getUserId())) {
            throw new SQLException("L'utilisateur avec l'ID " + panier.getUserId() + " n'existe pas");
        }
        if (!produitExists(panier.getProduitId())) {
            throw new SQLException("Le produit avec l'ID " + panier.getProduitId() + " n'existe pas");
        }

        String sql = "INSERT INTO panier (user_id, produit_id, quantity) VALUES (?, ?, ?)";
        
        try (PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setInt(1, panier.getUserId());
            pstmt.setInt(2, panier.getProduitId());
            pstmt.setInt(3, panier.getQuantity());
            
            pstmt.executeUpdate();
            
            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    panier.setId(generatedKeys.getInt(1));
                }
            }
        }
    }

    public void updateQuantity(int userId, int produitId, int newQuantity) throws SQLException {
        // Vérifier si l'utilisateur et le produit existent
        if (!userExists(userId)) {
            throw new SQLException("L'utilisateur avec l'ID " + userId + " n'existe pas");
        }
        if (!produitExists(produitId)) {
            throw new SQLException("Le produit avec l'ID " + produitId + " n'existe pas");
        }

        String sql = "UPDATE panier SET quantity = ? WHERE user_id = ? AND produit_id = ?";
        
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, newQuantity);
            pstmt.setInt(2, userId);
            pstmt.setInt(3, produitId);
            
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected == 0) {
                throw new SQLException("Aucune ligne n'a été mise à jour. L'article n'existe peut-être pas dans le panier.");
            }
        }
    }

    public List<Panier> getPanierByUserId(int userId) throws SQLException {
        List<Panier> items = new ArrayList<>();
        String sql = "SELECT * FROM panier WHERE user_id = ?";
        
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Panier item = new Panier();
                    item.setId(rs.getInt("id"));
                    item.setUserId(rs.getInt("user_id"));
                    item.setProduitId(rs.getInt("produit_id"));
                    item.setQuantity(rs.getInt("quantity"));
                    items.add(item);
                }
            }
        }
        return items;
    }

    public void supprimerDuPanier(int userId, int produitId) throws SQLException {
        String sql = "DELETE FROM panier WHERE user_id = ? AND produit_id = ?";
        
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            pstmt.setInt(2, produitId);
            
            pstmt.executeUpdate();
        }
    }

    public void viderPanier(int userId) throws SQLException {
        String sql = "DELETE FROM panier WHERE user_id = ?";
        
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            
            pstmt.executeUpdate();
        }
    }

    public boolean produitExisteDansPanier(int userId, int produitId) throws SQLException {
        String sql = "SELECT COUNT(*) FROM panier WHERE user_id = ? AND produit_id = ?";
        
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            pstmt.setInt(2, produitId);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        }
        return false;
    }
}
