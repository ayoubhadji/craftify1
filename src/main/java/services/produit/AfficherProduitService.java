package services.produit;



import entity.Produit;

import java.util.List;

public interface AfficherProduitService {
    List<Produit> getAllProduits();
    Produit getProduitById(int id);
    List<Produit> getProduitsByStock(int stockMin);
    List<Produit> getProduitsByPrix(double prixMax);
} 