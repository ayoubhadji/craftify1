package com.example.pidev2.service;

import com.example.pidev2.models.SliderItem;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SliderItemService {
    private final Connection connection;

    public SliderItemService() {
        try {
            // Initialize database connection
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/craftify", "root", "");
        } catch (SQLException e) {
            throw new RuntimeException("Failed to connect to the database", e);
        }
    }

    public void addSliderItem(SliderItem sliderItem) {
        String query = "INSERT INTO slider_item (foire_id, image_path, alt_text, url, position) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, sliderItem.getFoireId());
            stmt.setString(2, sliderItem.getImagePath());
            stmt.setString(3, sliderItem.getAltText());
            stmt.setString(4, sliderItem.getUrl());
            stmt.setInt(5, sliderItem.getPosition());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<SliderItem> getAllSliderItems() {
        List<SliderItem> sliderItems = new ArrayList<>();
        String query = "SELECT * FROM slider_item";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                SliderItem sliderItem = new SliderItem();
                sliderItem.setId(rs.getInt("id"));
                sliderItem.setFoireId(rs.getInt("foire_id"));
                sliderItem.setImagePath(rs.getString("image_path"));
                sliderItem.setAltText(rs.getString("alt_text"));
                sliderItem.setUrl(rs.getString("url"));
                sliderItem.setPosition(rs.getInt("position"));
                sliderItems.add(sliderItem);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return sliderItems;
    }

    public void deleteSliderItem(int id) {
        String query = "DELETE FROM slider_item WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateSliderItem(SliderItem sliderItem) {
        String query = "UPDATE slider_item SET foire_id = ?, image_path = ?, alt_text = ?, url = ?, position = ? WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, sliderItem.getFoireId());
            stmt.setString(2, sliderItem.getImagePath());
            stmt.setString(3, sliderItem.getAltText());
            stmt.setString(4, sliderItem.getUrl());
            stmt.setInt(5, sliderItem.getPosition());
            stmt.setInt(6, sliderItem.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
