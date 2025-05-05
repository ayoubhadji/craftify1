package services.blog;

import entity.Post;
import entity.Commentaire;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PostService {

    private Connection connection;

    public PostService(Connection connection) {
        this.connection = connection;
    }

    // Get all posts
    public List<Post> getAllPosts() throws SQLException {
        List<Post> posts = new ArrayList<>();
        String query = "SELECT * FROM post";
        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery(query);

        while (rs.next()) {
            Post post = new Post(
                    rs.getInt("id"),
                    rs.getInt("id_user_id"),
                    rs.getString("type_post"),
                    rs.getString("contenu"),
                    rs.getString("media_url"),
                    rs.getTimestamp("date_publication").toLocalDateTime(),
                    rs.getString("tranche_dage"),
                    rs.getInt("nmb_like")
            );
            posts.add(post);
        }
        return posts;
    }

    // Get comments by post ID
    public List<Commentaire> getCommentairesByPostId(int postId) throws SQLException {
        List<Commentaire> commentaires = new ArrayList<>();
        String query = "SELECT * FROM commentaire WHERE id_post_id = ?";
        PreparedStatement stmt = connection.prepareStatement(query);
        stmt.setInt(1, postId);
        ResultSet rs = stmt.executeQuery();

        while (rs.next()) {
            Commentaire commentaire = new Commentaire(
                    rs.getInt("id"),
                    rs.getInt("id_post_id"),
                    rs.getInt("id_user_id"),
                    rs.getString("contenu"),
                    rs.getTimestamp("date_commentaire").toLocalDateTime(),
                    rs.getInt("nmb_like")
            );
            commentaires.add(commentaire);
        }

        return commentaires;
    }

    // Add a comment
    public void addComment(Commentaire commentaire) throws SQLException {
        String query = "INSERT INTO commentaire (id_post_id, id_user_id, contenu, date_commentaire, nmb_like) VALUES (?, ?, ?, ?, ?)";
        PreparedStatement stmt = connection.prepareStatement(query);
        stmt.setInt(1, commentaire.getPostId());
        stmt.setInt(2, commentaire.getUserId());
        stmt.setString(3, commentaire.getContenu());
        stmt.setTimestamp(4, Timestamp.valueOf(commentaire.getDateCommentaire()));
        stmt.setInt(5, commentaire.getNmbLike());
        stmt.executeUpdate();
    }

    // Add a post
    public void addPost(Post post) throws SQLException {
        String query = "INSERT INTO post (id_user_id, type_post, contenu, media_url, date_publication, tranche_dage, nmb_like) VALUES (?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement stmt = connection.prepareStatement(query);
        stmt.setInt(1, post.getIdUser());
        stmt.setString(2, post.getTypePost());
        stmt.setString(3, post.getContenu());
        stmt.setString(4, post.getMediaUrl());
        stmt.setTimestamp(5, Timestamp.valueOf(post.getDatePublication()));
        stmt.setString(6, post.getTrancheDage());
        stmt.setInt(7, post.getNmbLike());
        stmt.executeUpdate();
    }

    // Delete a post
    public void deletePost(int postId) throws SQLException {
        // 1. Delete related comments first
        String deleteCommentsQuery = "DELETE FROM commentaire WHERE id_post_id = ?";
        try (PreparedStatement deleteCommentsStmt = connection.prepareStatement(deleteCommentsQuery)) {
            deleteCommentsStmt.setInt(1, postId);
            deleteCommentsStmt.executeUpdate();
        }

        // 2. Then delete the post
        String deletePostQuery = "DELETE FROM post WHERE id = ?";
        try (PreparedStatement deletePostStmt = connection.prepareStatement(deletePostQuery)) {
            deletePostStmt.setInt(1, postId);
            deletePostStmt.executeUpdate();
        }
    }


    // Update a post
    public void updatePost(Post post) throws SQLException {
        String query = "UPDATE post SET type_post = ?, contenu = ?, media_url = ?, date_publication = ?, tranche_dage = ?, nmb_like = ? WHERE id = ?";
        PreparedStatement stmt = connection.prepareStatement(query);
        stmt.setString(1, post.getTypePost());
        stmt.setString(2, post.getContenu());
        stmt.setString(3, post.getMediaUrl());
        stmt.setTimestamp(4, Timestamp.valueOf(post.getDatePublication()));
        stmt.setString(5, post.getTrancheDage());
        stmt.setInt(6, post.getNmbLike());
        stmt.setInt(7, post.getIdPost());
        stmt.executeUpdate();
    }

    // Get a post by ID
    public Post getPostById(int postId) throws SQLException {
        String query = "SELECT * FROM post WHERE id = ?";
        PreparedStatement stmt = connection.prepareStatement(query);
        stmt.setInt(1, postId);
        ResultSet rs = stmt.executeQuery();

        if (rs.next()) {
            return new Post(
                    rs.getInt("id"),
                    rs.getInt("id_user_id"),
                    rs.getString("type_post"),
                    rs.getString("contenu"),
                    rs.getString("media_url"),
                    rs.getTimestamp("date_publication").toLocalDateTime(),
                    rs.getString("tranche_dage"),
                    rs.getInt("nmb_like")
            );
        }
        return null;
    }



}
