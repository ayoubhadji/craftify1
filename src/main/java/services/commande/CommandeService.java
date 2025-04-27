package services.commande;

import entity.Commande;
import java.util.List;

public interface CommandeService {
    List<Commande> getAllCommandes();
    void save(Commande commande);
    void update(Commande commande);
    void delete(int id);
}
