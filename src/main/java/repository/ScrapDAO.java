package repository;

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

    // ✅ 특정 유저의 스크랩한 레시피 ID 목록
    public List<String> findScrappedRecipeIdsByUser(String userId) throws SQLException {
        List<String> list = new ArrayList<>();
        String sql = "SELECT recipe_id FROM Scrap WHERE user_id = ?";

        try (Connection con = db.open();
             PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setString(1, userId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    list.add(rs.getString("recipe_id"));
                }
            }
        }
        return list;
    }
}
