package services.produit;


import utils.MyDataBase;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class SupprimerProduitServiceImpl implements SupprimerProduitService {
    
    @Override
    public void supprimerProduit(int id) {
        String sql = "DELETE FROM produit WHERE id = ?";

        try (Connection conn = MyDataBase.getInstance().getMyConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Erreur lors de la suppression du produit : " + e.getMessage());
            e.printStackTrace();
        }
    }
} 