package repository;

import dto.recipe.RecipeDTO;
import utils.DBConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ScrapDAO {
    private final DBConnection db = new DBConnection();

    /** ✅ 스크랩 추가 (중복 시 무시) */
    public void addScrap(String userId, String recipeId) throws SQLException {
        String sql = "INSERT IGNORE INTO Scrap (recipe_id, user_id) VALUES (?, ?)";

        try (Connection con = db.open();
             PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setString(1, recipeId);
            pstmt.setString(2, userId);
            pstmt.executeUpdate();
        }
    }

    /** ✅ 스크랩 제거 */
    public void removeScrap(String userId, String recipeId) throws SQLException {
        String sql = "DELETE FROM Scrap WHERE recipe_id = ? AND user_id = ?";

        try (Connection con = db.open();
             PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setString(1, recipeId);
            pstmt.setString(2, userId);
            pstmt.executeUpdate();
        }
    }

    /** ✅ 스크랩 여부 확인 */
    public boolean isScrapped(String userId, String recipeId) throws SQLException {
        String sql = "SELECT 1 FROM Scrap WHERE recipe_id = ? AND user_id = ?";
        try (Connection con = db.open();
             PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setString(1, recipeId);
            pstmt.setString(2, userId);
            try (ResultSet rs = pstmt.executeQuery()) {
                return rs.next(); // 존재하면 true
            }
        }
    }
    public List<String> findScrappedRecipeIdsByUser(String userId) throws SQLException {
        List<String> recipeIds = new ArrayList<>();

        String sql = "SELECT recipe_id FROM Scrap WHERE user_id = ? ORDER BY created_at DESC";

        try (Connection con = db.open();
             PreparedStatement pstmt = con.prepareStatement(sql)) {

            pstmt.setString(1, userId);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    recipeIds.add(rs.getString("recipe_id"));
                }
            }
        }

        return recipeIds;
    }


    // ✅ 특정 유저의 스크랩한 레시피 목록
    // 뭘 쓸지 몰라서 일단 recipeDTO 다 가져옴.
    public List<RecipeDTO> findScrappedRecipesByUser(String userId) throws SQLException {
        List<RecipeDTO> list = new ArrayList<>();

        String sql = """
        SELECT r.recipe_id, r.user_id, r.title, r.thumbnail_image_url,
               r.views, r.people_count, r.prep_time, r.cook_time, r.`like`, r.kcal
        FROM Scrap s
        JOIN Recipe r ON s.recipe_id = r.recipe_id
        WHERE s.user_id = ?
        ORDER BY s.created_at DESC
    """;

        try (Connection con = db.open();
             PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setString(1, userId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    RecipeDTO recipe = new RecipeDTO();
                    recipe.setRecipe_id(rs.getString("recipe_id"));
                    recipe.setUser_id(rs.getString("user_id"));
                    recipe.setTitle(rs.getString("title"));
                    recipe.setThumbnail_image_url(rs.getString("thumbnail_image_url"));
                    recipe.setViews(rs.getInt("views"));
                    recipe.setPeople_count(rs.getInt("people_count"));
                    recipe.setPrep_time(rs.getInt("prep_time"));
                    recipe.setCook_time(rs.getInt("cook_time"));
                    recipe.setLike(rs.getInt("like"));
                    recipe.setKcal(rs.getInt("kcal"));

                    list.add(recipe);
                }
            }
        }
        return list;
    }

}
