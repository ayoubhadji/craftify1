package entity;

public class Panier {
    private int id;
    private int userId;
    private int produitId;
    private int quantity;

    public Panier() {
    }

    public Panier(int userId, int produitId, int quantity) {
        this.userId = userId;
        this.produitId = produitId;
        this.quantity = quantity;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getProduitId() {
        return produitId;
    }

    public void setProduitId(int produitId) {
        this.produitId = produitId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return "Panier{" +
                "id=" + id +
                ", userId=" + userId +
                ", produitId=" + produitId +
                ", quantity=" + quantity +
                '}';
    }
} 