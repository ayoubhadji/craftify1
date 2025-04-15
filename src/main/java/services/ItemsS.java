package services;

import models.Items;
import services.Crud;
import utils.MyDb;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ItemsS implements Crud<Items> {

    private final Connection cnx;

    public ItemsS() {
        cnx = MyDb.getInstance().getConn();
    }

    @Override
    public void create(Items item) throws Exception {
        String req = "INSERT INTO slider_item (foire_id, image_path, alt_text, url, position) " +
                "VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement pst = cnx.prepareStatement(req)) {
            pst.setInt(1, item.getFoire_id());
            pst.setString(2, item.getImage_path());
            pst.setString(3, item.getAlt_text());
            pst.setString(4, item.getUrl());
            pst.setInt(5, item.getPosition());

            pst.executeUpdate();
        }
    }

    @Override
    public void update(Items item) throws Exception {
        String req = "UPDATE slider_item SET foire_id=?, image_path=?, alt_text=?, url=?, position=? WHERE id=?";
        try (PreparedStatement pst = cnx.prepareStatement(req)) {
            pst.setInt(1, item.getFoire_id());
            pst.setString(2, item.getImage_path());
            pst.setString(3, item.getAlt_text());
            pst.setString(4, item.getUrl());
            pst.setInt(5, item.getPosition());
            pst.setInt(6, item.getId());

            pst.executeUpdate();
        }
    }

    @Override
    public void delete(Items item) throws Exception {
        String req = "DELETE FROM slider_item WHERE id=?";
        try (PreparedStatement pst = cnx.prepareStatement(req)) {
            pst.setInt(1, item.getId());
            pst.executeUpdate();
        }
    }

    @Override
    public List<Items> getAll() {
        List<Items> list = new ArrayList<>();
        String req = "SELECT * FROM slider_item";
        try (Statement ste = cnx.createStatement();
             ResultSet rs = ste.executeQuery(req)) {

            while (rs.next()) {
                Items item = new Items();
                item.setId(rs.getInt("id"));
                item.setFoire_id(rs.getInt("foire_id"));
                item.setImage_path(rs.getString("image_path"));
                item.setAlt_text(rs.getString("alt_text"));
                item.setUrl(rs.getString("url"));
                item.setPosition(rs.getInt("position"));

                list.add(item);
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Ou loggue le message d’erreur
        }
        return list;
    }

    public Items readById(int id) throws Exception {
        String req = "SELECT * FROM slider_item WHERE id=?";
        try (PreparedStatement pst = cnx.prepareStatement(req)) {
            pst.setInt(1, id);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    Items item = new Items();
                    item.setId(rs.getInt("id"));
                    item.setFoire_id(rs.getInt("foire_id"));
                    item.setImage_path(rs.getString("image_path"));
                    item.setAlt_text(rs.getString("alt_text"));
                    item.setUrl(rs.getString("url"));
                    item.setPosition(rs.getInt("position"));
                    return item;
                }
            }
        }
        return null;
    }
    public List<Items> getItemsByFoireId(int foireId) {
        List<Items> list = new ArrayList<>();
        String req = "SELECT * FROM slider_item WHERE foire_id = ?";
        try (PreparedStatement pst = cnx.prepareStatement(req)) {
            pst.setInt(1, foireId);
            try (ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    Items item = new Items();
                    item.setId(rs.getInt("id"));
                    item.setImage_path(rs.getString("image_path"));
                    item.setAlt_text(rs.getString("alt_text"));
                    item.setUrl(rs.getString("url"));
                    item.setFoire_id(rs.getInt("foire_id"));
                    list.add(item);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}
