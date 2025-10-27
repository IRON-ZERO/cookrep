package service;

import dto.recipe.RecipeDTO;
import repository.ScrapDAO;
import utils.PresignedUrlGenerator;

import java.sql.SQLException;
import java.util.List;

public class ScrapService {
    private final ScrapDAO scrapDAO = new ScrapDAO();
    private final PresignedUrlGenerator presigner = PresignedUrlGenerator.getInstance();

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

    public List<RecipeDTO> findScrappedRecipesByUser(String userId) throws SQLException {
        List<RecipeDTO> recipes = scrapDAO.findScrappedRecipesByUser(userId);
        for (RecipeDTO recipe : recipes) {
            String key = recipe.getThumbnail_image_url();

            // DB에는 S3의 object key가 들어있을 거니까, presigned 변환
            if (key != null && !key.startsWith("https://")) {
                String presignedUrl = presigner.generatePresignedUrl(key);
                recipe.setThumbnail_image_url(presignedUrl);
            }
        }

        return recipes;
    }
}
