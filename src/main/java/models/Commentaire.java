package models;

import java.time.LocalDateTime;

public class Commentaire {
    private int id;
    private int postId;
    private int userId;
    private String contenu;
    private LocalDateTime dateCommentaire;
    private int nmbLike;

    public Commentaire() {
    }

    public Commentaire(int id, int postId, int userId, String contenu,
                       LocalDateTime dateCommentaire, int nmbLike) {
        this.id = id;
        this.postId = postId;
        this.userId = userId;
        this.contenu = contenu;
        this.dateCommentaire = dateCommentaire;
        this.nmbLike = nmbLike;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getPostId() { return postId; }
    public void setPostId(int postId) { this.postId = postId; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public String getContenu() { return contenu; }
    public void setContenu(String contenu) { this.contenu = contenu; }

    public LocalDateTime getDateCommentaire() { return dateCommentaire; }
    public void setDateCommentaire(LocalDateTime dateCommentaire) { this.dateCommentaire = dateCommentaire; }

    public int getNmbLike() { return nmbLike; }
    public void setNmbLike(int nmbLike) { this.nmbLike = nmbLike; }

    @Override
    public String toString() {
        return "Commentaire{" +
                "id=" + id +
                ", postId=" + postId +
                ", userId=" + userId +
                ", contenu='" + contenu + '\'' +
                ", dateCommentaire=" + dateCommentaire +
                ", nmbLike=" + nmbLike +
                '}';
    }
}
