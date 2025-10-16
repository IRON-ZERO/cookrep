package service;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import dto.recipe.RecipeDTO;
import repository.RecipeDAO;
import utils.PresignedUrlGenerator;

public class RecipeService {
	private final RecipeDAO recipeDAO = new RecipeDAO();
    private final PresignedUrlGenerator presigner = PresignedUrlGenerator.getInstance();
	/**
	 * 냉장고 재료 기반 레시피 추천
	 * - RecipeDTO를 key, 일치 재료 수를 value로 반환
	 * - 일치 재료 개수(matchCount) 기준으로 정렬된 LinkedHashMap 반환
	 */
    public Map<RecipeDTO, Integer> recommendWithMatchCount(List<String> ingredientNames) throws SQLException {
        Map<RecipeDTO, Integer> result = recipeDAO.findRecipesWithMatchCount(ingredientNames);
        System.out.println("여기서 일단 멈춤");

        // Presigned URL 변환
        for (RecipeDTO recipe : result.keySet()) {
            String key = recipe.getThumbnail_image_url();
            if (key != null && !key.startsWith("https://")) {
                // 이미 presigned면 건너뜀
                System.out.println("hello!! "+key);
                String presignedUrl = presigner.generatePresignedUrl(key);
                System.out.println(presignedUrl);
                recipe.setThumbnail_image_url(presignedUrl);
            }
        }
        return result;
    }
}

