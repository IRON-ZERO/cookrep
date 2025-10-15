package repository;

import utils.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class RecipeIngredientDAO {
    private Connection conn = null;

    public RecipeIngredientDAO() {
        DBConnection dbConnection = new DBConnection();
        dbConnection.open();
        this.conn = dbConnection.getConnection();
    }
    public RecipeIngredientDAO(Connection conn) {}

    // 개별 insert
    public void addIngredient(String recipeId, int ingredientId, String count) throws SQLException {
        String sql = "INSERT IGNORE INTO RecipeIngredient (recipe_id, ingredient_id, count) VALUES (?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, recipeId);
            pstmt.setInt(2, ingredientId);
            pstmt.setString(3, count);
            pstmt.executeUpdate();
        }
        conn.close();
    }

    // 여러 재료 한 번에 추가 (batch)
    public void addIngredientsBatch(String recipeId, int[] ingredientIds, String[] counts) throws SQLException {
        String sql = "INSERT IGNORE INTO RecipeIngredient (recipe_id, ingredient_id, count) VALUES (?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            for (int i = 0; i < ingredientIds.length; i++) {
                pstmt.setString(1, recipeId);
                pstmt.setInt(2, ingredientIds[i]);
                pstmt.setString(3, counts[i]);
                pstmt.addBatch();
            }
            pstmt.executeBatch();
        }
        conn.close();
    }
}
