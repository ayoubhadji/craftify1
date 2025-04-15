package services;

import models.Commentaire;
import java.sql.*;
import java.util.*;

public class CommentaireService {
    private Connection conn;

    public CommentaireService(Connection conn) {
        this.conn = conn;
    }

    public void addCommentaire(Commentaire c) throws SQLException {
        String sql = "INSERT INTO commentaire (id_post_id, id_user_id, contenu, date_commentaire, nmb_like) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, c.getPostId());
            stmt.setInt(2, c.getUserId());
            stmt.setString(3, c.getContenu());
            stmt.setTimestamp(4, Timestamp.valueOf(c.getDateCommentaire()));
            stmt.setInt(5, c.getNmbLike());
            stmt.executeUpdate();
        }
    }

    public void updateCommentaire(Commentaire c) throws SQLException {
        String sql = "UPDATE commentaire SET contenu=?, date_commentaire=?, nmb_like=? WHERE id=?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, c.getContenu());
            stmt.setTimestamp(2, Timestamp.valueOf(c.getDateCommentaire()));
            stmt.setInt(3, c.getNmbLike());
            stmt.setInt(4, c.getId());
            stmt.executeUpdate();
        }
    }

    public void deleteCommentaire(int id) throws SQLException {
        String sql = "DELETE FROM commentaire WHERE id=?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    public List<Commentaire> getAllCommentaires() throws SQLException {
        List<Commentaire> list = new ArrayList<>();
        String sql = "SELECT * FROM commentaire";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Commentaire c = new Commentaire(
                        rs.getInt("id"),
                        rs.getInt("id_post_id"),
                        rs.getInt("id_user_id"),
                        rs.getString("contenu"),
                        rs.getTimestamp("date_commentaire").toLocalDateTime(),
                        rs.getInt("nmb_like")
                );
                list.add(c);
            }
        }
        return list;
    }
}
