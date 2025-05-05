package entity;

public class SliderItem {
    private int id;
    private int foireId;
    private String imagePath;
    private String altText;
    private String url;
    private int position;

    // Getters and setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getFoireId() {
        return foireId;
    }

    public void setFoireId(int foireId) {
        this.foireId = foireId;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getAltText() {
        return altText;
    }

    public void setAltText(String altText) {
        this.altText = altText;
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

    @Override
    public String toString() {
        return "Foire ID: " + foireId +
                "\nImage Path: " + imagePath +
                "\nAlt Text: " + altText +
                "\nURL: " + url +
                "\nPosition: " + position;
    }
}
