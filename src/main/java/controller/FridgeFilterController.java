package controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import dto.recipe.RecipeDTO;
import service.RecipeService;
import service.ScrapService;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.*;

@WebServlet("/mypage/fridge/filter")
public class FridgeFilterController extends HttpServlet {
    private final RecipeService recipeService = new RecipeService();
    private final ScrapService scrapService = new ScrapService();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json;charset=UTF-8");
        ObjectMapper mapper = new ObjectMapper();

        HttpSession session = req.getSession(false);
        String userId = (String) session.getAttribute("userId");

        try {
            JsonNode json = mapper.readTree(req.getInputStream());
            List<String> ingredients = new ArrayList<>();
            json.get("ingredients").forEach(node -> ingredients.add(node.asText()));

            // 선택된 재료 기반 추천 재계산
            Map<RecipeDTO, Integer> recommendMap = recipeService.recommendWithMatchCount(ingredients);

            // DTO를 단순한 JSON으로 변환
            Map<String, Object> responseMap = new HashMap<>();
            Map<String, Object> recipes = new LinkedHashMap<>();
            for (RecipeDTO r : recommendMap.keySet()) {
                Map<String, Object> rec = new HashMap<>();
                rec.put("title", r.getTitle());
                rec.put("thumbnail_image_url", r.getThumbnail_image_url());
                rec.put("recipe_id", r.getRecipe_id());
                recipes.put(mapper.writeValueAsString(r), recommendMap.get(r));
            }

            // 스크랩 상태 Map (key = recipe_id, value = true/false)
            Map<String, Boolean> scrapStatusMap = new HashMap<>();
            List<String> scrappedIds = scrapService.getScrappedRecipeIdsByUser(userId);

            for (RecipeDTO recipe : recommendMap.keySet()) {
                scrapStatusMap.put(recipe.getRecipe_id(),
                    scrappedIds.contains(recipe.getRecipe_id()));
            }

            responseMap.put("scrapStatusMap", scrapStatusMap);
            responseMap.put("recommendMap", recipes);
            resp.getWriter().write(mapper.writeValueAsString(responseMap));

        } catch (Exception e) {
            resp.setStatus(500);
            resp.getWriter().write("{\"error\": \"추천 로직 실패: " + e.getMessage() + "\"}");
        }
    }
}
