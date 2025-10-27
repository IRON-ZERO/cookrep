package repository;

import model.Ingredient;
import model.UserIngredient;
import utils.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserIngredientDAO {
    private Connection conn = null;

    public UserIngredientDAO() {
        DBConnection dbConnection = new DBConnection();
        dbConnection.open();
        this.conn = dbConnection.getConnection();
    }
    public UserIngredientDAO(Connection conn){
        this.conn = conn;
    }
    public void addIngredientsBatch(String userId, int[] ingredientIds) throws SQLException {
        String sql = "INSERT INTO userIngredient (user_id, ingredient_id) VALUES (?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            for (int id : ingredientIds) {
                ps.setString(1, userId);
                ps.setInt(2, id);
                ps.addBatch();
            }
            ps.executeBatch();
        }
        conn.close();
    }
    public void addUserIngredient(String userId, int ingredientId) throws SQLException {
        // 1 -> user_id  2 -> ingredient_id
        String sql = "INSERT INTO userIngredient VALUES (?, ?)";

        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setString(1, userId);
        pstmt.setInt(2, ingredientId);
        pstmt.executeUpdate();
        conn.close();
    }
    public void removeUserIngredient(String userId, int ingredientId) throws SQLException {
        String sql = "DELETE FROM userIngredient WHERE user_id = ? AND ingredient_id = ?";
        try(PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, userId);
            pstmt.setInt(2, ingredientId);
            pstmt.executeUpdate();
        }
        conn.close();
    }

    public List<UserIngredient> findByUserId(String userId) throws SQLException {
        String sql = "SELECT * FROM userIngredient WHERE user_id = ?";
        List<UserIngredient> ingredients = new ArrayList<>();
        IngredientDAO ingredientDAO = new IngredientDAO();
        try(PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, userId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                UserIngredient userIngredient = new UserIngredient();
                int ingredientId = rs.getInt("ingredient_id");
                Ingredient ingredient = ingredientDAO.findById(ingredientId);
                userIngredient.setUserId(rs.getString("user_id"));
                userIngredient.setIngredient(ingredient);
                userIngredient.setIngredientId(ingredientId);
                ingredients.add(userIngredient);
            }
        }
        conn.close();
        return ingredients;
    }

}
