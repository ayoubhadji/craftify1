package com.artisanat.services;

import com.artisanat.entities.Commande;
import java.util.List;

public interface CommandeService {
    Commande creerCommande(Commande commande);
    Commande modifierCommande(Commande commande);
    void annulerCommande(int id);
    Commande getCommandeById(int id);
    List<Commande> getAllCommandes();
    List<Commande> getCommandesByClient(int clientId);
    List<Commande> getCommandesByStatut(String statut);
    double calculerMontantTotal(int commandeId);
} 