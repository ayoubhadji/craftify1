package models;

public class Items {
    private int id;
    private int foire_id;
    private String image_path;
    private String alt_text;
    private String url;
    private int position;

    // Constructeur vide
    public Items() {}

    // Constructeur complet
    public Items(int id, int foire_id, String image_path, String alt_text, String url, int position) {
        this.id = id;
        this.foire_id = foire_id;
        this.image_path = image_path;
        this.alt_text = alt_text;
        this.url = url;
        this.position = position;
    }

    // Getters et Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getFoire_id() {
        return foire_id;
    }

    public void setFoire_id(int foire_id) {
        this.foire_id = foire_id;
    }

    public String getImage_path() {
        return image_path;
    }

    public void setImage_path(String image_path) {
        this.image_path = image_path;
    }

    public String getAlt_text() {
        return alt_text;
    }

    public void setAlt_text(String alt_text) {
        this.alt_text = alt_text;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    // toString pour debug ou affichage
    @Override
    public String toString() {
        return "Items{" +
                "id=" + id +
                ", foire_id=" + foire_id +
                ", image_path='" + image_path + '\'' +
                ", alt_text='" + alt_text + '\'' +
                ", url='" + url + '\'' +
                ", position=" + position +
                '}';
    }
}
