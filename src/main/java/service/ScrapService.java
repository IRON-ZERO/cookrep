package service;

import repository.ScrapDAO;
import java.sql.SQLException;
import java.util.List;

public class ScrapService {
    private final ScrapDAO scrapDAO = new ScrapDAO();

    public void addScrap(String userId, String recipeId) throws SQLException {
        scrapDAO.addScrap(userId, recipeId);
    }

    public void removeScrap(String userId, String recipeId) throws SQLException {
        scrapDAO.removeScrap(userId, recipeId);
    }

    public boolean isScrapped(String userId, String recipeId) throws SQLException {
        return scrapDAO.isScrapped(userId, recipeId);
    }

    // ✅ 사용자의 스크랩 레시피 ID 목록 조회
    public List<String> getScrappedRecipeIdsByUser(String userId) throws SQLException {
        return scrapDAO.findScrappedRecipeIdsByUser(userId);
    }
}
