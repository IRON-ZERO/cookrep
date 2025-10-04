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

    public void addUserIngredient(String userId, int ingredientId) throws SQLException {
        // 1 -> user_id  2 -> ingredeint_id
        String sql = "INSERT INTO userIngredient VALUES (?, ?)";

        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setString(1, userId);
        pstmt.setInt(2, ingredientId);
        pstmt.executeUpdate();
    }
    public void removeUserIngredient(String userId, int ingredientId) throws SQLException {
        String sql = "DELETE FROM userIngredient WHERE user_id = ? AND ingredient_id = ?";
        try(PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, userId);
            pstmt.setInt(2, ingredientId);
            pstmt.executeUpdate();
        }
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
        return ingredients;
    }

}
