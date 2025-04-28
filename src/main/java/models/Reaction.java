package models;

public class Reaction {
    private int id;
    private int idUser;
    private int idPost;
    private String type; // "like" or "dislike"

    public Reaction() {}

    public Reaction(int id, int idUser, int idPost, String type) {
        this.id = id;
        this.idUser = idUser;
        this.idPost = idPost;
        this.type = type;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getIdUser() { return idUser; }
    public void setIdUser(int idUser) { this.idUser = idUser; }

    public int getIdPost() { return idPost; }
    public void setIdPost(int idPost) { this.idPost = idPost; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    @Override
    public String toString() {
        return "Reaction{" +
                "id=" + id +
                ", idUser=" + idUser +
                ", idPost=" + idPost +
                ", type='" + type + '\'' +
                '}';
    }
}
