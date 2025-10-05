package controller.recipe;

import com.fasterxml.jackson.databind.ObjectMapper;
import dto.recipe.RecipeDTO;
import dto.recipe.RecipeDTO.Step;
import repository.RecipeDAO;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@WebServlet("/recipe/update")
public class RecipeUpdateController extends HttpServlet {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final RecipeDAO recipeDAO = new RecipeDAO();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json;charset=UTF-8");
        request.setCharacterEncoding("UTF-8");

        try (BufferedReader reader = request.getReader()) {
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }

            // JSON 파싱
            Map<String, Object> reqMap = objectMapper.readValue(sb.toString(), Map.class);

            // 레시피 기본 정보
            String recipeId = reqMap.get("recipe_id") != null ? reqMap.get("recipe_id").toString() : null;

            if (recipeId == null) {
                response.setStatus(400);
                response.getWriter().write("{\"error\":\"recipe_id is missing\"}");
                return;
            }
            String title = (String) reqMap.get("title");
            String thumbnailUrl = (String) reqMap.get("thumbnail_url"); // 프론트에서 전달된 URL

            // steps 처리
            List<Map<String, Object>> stepsList = (List<Map<String, Object>>) reqMap.get("steps");
            List<Step> steps = new ArrayList<>();
            if (stepsList != null) {
                for (int i = 0; i < stepsList.size(); i++) {
                    Map<String, Object> stepMap = stepsList.get(i);
                    String content = (String) stepMap.get("content");
                    String imageUrl = (String) stepMap.get("imageUrl");

                    Step step = new Step();
                    step.setStepOrder(i + 1);
                    step.setContents(content);
                    step.setImageUrl(imageUrl);

                    steps.add(step);
                }
            }

            // DTO에 담기
            RecipeDTO recipe = new RecipeDTO();
            recipe.setRecipe_id(recipeId);
            recipe.setTitle(title);
            recipe.setThumbnail_image_url(thumbnailUrl);
            recipe.setSteps(steps);

            // DB 업데이트
            recipeDAO.updateRecipeWithSteps(recipe);

            // 성공 응답
            response.getWriter().write("{\"result\":\"success\"}");

        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(500);
            response.getWriter().write("{\"error\":\"" + e.getMessage() + "\"}");
        }
    }
}
