package entity;

import java.time.LocalDateTime;



public class Post {
    private int id;
    private int userId;
    private String typePost;
    private String contenu;
    private String mediaUrl;
    private LocalDateTime datePublication;
    private String trancheDage;
    private int nmbLike;

    public Post() {
    }

    public Post(int id, int userId, String typePost, String contenu, String mediaUrl,
                LocalDateTime datePublication, String trancheDage, int nmbLike) {
        this.id = id;
        this.userId = userId;
        this.typePost = typePost;
        this.contenu = contenu;
        this.mediaUrl = mediaUrl;
        this.datePublication = datePublication;
        this.trancheDage = trancheDage;
        this.nmbLike = nmbLike;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public String getTypePost() { return typePost; }
    public void setTypePost(String typePost) { this.typePost = typePost; }

    public String getContenu() { return contenu; }
    public void setContenu(String contenu) { this.contenu = contenu; }

    public String getMediaUrl() { return mediaUrl; }
    public void setMediaUrl(String mediaUrl) { this.mediaUrl = mediaUrl; }

    public LocalDateTime getDatePublication() { return datePublication; }
    public void setDatePublication(LocalDateTime datePublication) { this.datePublication = datePublication; }

    public String getTrancheDage() { return trancheDage; }
    public void setTrancheDage(String trancheDage) { this.trancheDage = trancheDage; }

    public int getNmbLike() { return nmbLike; }
    public void setNmbLike(int nmbLike) { this.nmbLike = nmbLike; }

    @Override
    public String toString() {
        return "Post{" +
                "id=" + id +
                ", userId=" + userId +
                ", typePost='" + typePost + '\'' +
                ", contenu='" + contenu + '\'' +
                ", mediaUrl='" + mediaUrl + '\'' +
                ", datePublication=" + datePublication +
                ", trancheDage='" + trancheDage + '\'' +
                ", nmbLike=" + nmbLike +
                '}';
    }

    public int getIdPost() {
        return id;
    }

    // (Optional) Add a setter if you need it
    public void setIdPost(int idPost) {
        this.id = idPost;
    }
    public int getIdUser() {
        return userId;
    }

}

