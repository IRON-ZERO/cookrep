package service;

import dto.recipe.RecipeDTO;
import repository.RecipeDAO;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class RecipeService {
    private final RecipeDAO recipeDAO = new RecipeDAO();

    /**
     * 냉장고 재료 기반 레시피 추천
     * - RecipeDTO를 key, 일치 재료 수를 value로 반환
     * - 일치 재료 개수(matchCount) 기준으로 정렬된 LinkedHashMap 반환
     */
    public Map<RecipeDTO, Integer> recommendWithMatchCount(List<String> ingredientNames) throws SQLException {
        return recipeDAO.findRecipesWithMatchCount(ingredientNames);
    }
}
