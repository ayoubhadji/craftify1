package services.produit;

import entity.Produit;
import java.util.List;

public interface ProduitService {
    List<Produit> getAllProduits();
    Produit getProduitById(int id);
    void addProduit(Produit produit);
    void updateProduit(Produit produit);
    void deleteProduit(int id);
}
