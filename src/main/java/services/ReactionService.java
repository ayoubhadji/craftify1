package services;

import models.Reaction;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ReactionService {
    private Connection conn;

    public ReactionService(Connection conn) {
        this.conn = conn;
    }

    public Reaction getUserReaction(int postId, int userId) throws Exception {
        String sql = "SELECT * FROM reaction WHERE id_post = ? AND id_user = ?";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setInt(1, postId);
        stmt.setInt(2, userId);
        ResultSet rs = stmt.executeQuery();
        if (rs.next()) {
            return new Reaction(
                    rs.getInt("id"),
                    rs.getInt("id_user"),
                    rs.getInt("id_post"),
                    rs.getString("type")
            );
        }
        return null;
    }

    public void react(int postId, int userId, String type) throws Exception {
        Reaction existing = getUserReaction(postId, userId);
        if (existing != null) {
            String sql = "UPDATE reaction SET type = ? WHERE id_post = ? AND id_user = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, type);
            stmt.setInt(2, postId);
            stmt.setInt(3, userId);
            stmt.executeUpdate();
        } else {
            String sql = "INSERT INTO reaction(id_user, id_post, type) VALUES (?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, userId);
            stmt.setInt(2, postId);
            stmt.setString(3, type);
            stmt.executeUpdate();
        }
    }

    public int countReactions(int postId, String type) throws Exception {
        String sql = "SELECT COUNT(*) FROM reaction WHERE id_post = ? AND type = ?";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setInt(1, postId);
        stmt.setString(2, type);
        ResultSet rs = stmt.executeQuery();
        if (rs.next()) return rs.getInt(1);
        return 0;
    }

    public List<Integer> getLikedPostIdsByUser(int userId) throws SQLException {
        List<Integer> postIds = new ArrayList<>();
        String sql = "SELECT id_post FROM reaction WHERE id_user = ? AND type = 'like'";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                postIds.add(rs.getInt("id_post"));
            }
        }
        return postIds;
    }

    public boolean hasReacted(int postId, int userId, String type) throws SQLException {
        String sql = "SELECT COUNT(*) FROM reaction WHERE id_post = ? AND id_user = ? AND type = ?";
        try (var ps = conn.prepareStatement(sql)) {
            ps.setInt(1, postId);
            ps.setInt(2, userId);
            ps.setString(3, type);
            var rs = ps.executeQuery();
            return rs.next() && rs.getInt(1) > 0;
        }
    }

    public void toggleReaction(int postId, int userId, String type) throws SQLException {
        if (hasReacted(postId, userId, type)) {
            // Remove reaction
            String deleteSQL = "DELETE FROM reaction WHERE id_post = ? AND id_user = ? AND type = ?";
            try (var ps = conn.prepareStatement(deleteSQL)) {
                ps.setInt(1, postId);
                ps.setInt(2, userId);
                ps.setString(3, type);
                ps.executeUpdate();
            }
        } else {
            // Add reaction
            String insertSQL = "INSERT INTO reaction (id_post, id_user, type) VALUES (?, ?, ?)";
            try (var ps = conn.prepareStatement(insertSQL)) {
                ps.setInt(1, postId);
                ps.setInt(2, userId);
                ps.setString(3, type);
                ps.executeUpdate();
            }
        }
    }


}
