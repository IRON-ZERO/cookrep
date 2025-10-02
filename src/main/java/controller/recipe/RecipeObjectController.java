package controller.recipe;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;
import dto.recipe.RecipeDTO;
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
import java.util.UUID;

@WebServlet("/recipe/register")
public class RecipeObjectController extends HttpServlet {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final RecipeDAO recipeDAO = new RecipeDAO();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json;charset=UTF-8");

        try {
            // JSON 읽기
            StringBuilder sb = new StringBuilder();
            try (BufferedReader reader = req.getReader()) {
                String line;
                while ((line = reader.readLine()) != null) {
                    sb.append(line);
                }
            }

            JsonNode root = objectMapper.readTree(sb.toString());

            // UUID로 recipeId 생성
            String recipeId = UUID.randomUUID().toString();

            String userId = root.get("userId").asText(); // u001 하드코딩 가능
            String title = root.get("title").asText();
            String mainImageUrl = root.get("mainImageUrl").asText();

            // Step 정보
            List<RecipeDTO.Step> steps = new ArrayList<>();
            if (root.has("steps")) {
                int stepOrder = 1;
                for (JsonNode stepNode : root.get("steps")) {
                    String content = stepNode.get("content").asText();
                    String imageUrl = stepNode.get("imageUrl").asText();

                    RecipeDTO.Step step = new RecipeDTO.Step();
                    step.setStepOrder(stepOrder);
                    step.setContents(content);
                    step.setImageUrl(imageUrl);
                    steps.add(step);

                    stepOrder++;
                }
            }

            // DTO 생성
            RecipeDTO recipe = new RecipeDTO();
            recipe.setRecipe_id(recipeId);  // UUID 세팅
            recipe.setTitle(title);
            recipe.setThumbnail_image_url(mainImageUrl);
            recipe.setSteps(steps);
//            recipe.setUserId(userId); // foreign key 충족 위해 필요

            // DAO 호출
            recipeDAO.insertRecipeWithSteps(recipe);

            // 생성된 recipeId 반환
            resp.getWriter().write("{\"status\":\"success\",\"recipeId\":\"" + recipeId + "\"}");

        } catch (Exception e) {
            e.printStackTrace();
            resp.setStatus(500);
            resp.getWriter().write("{\"status\":\"error\",\"message\":\"" + e.getMessage() + "\"}");
        }
    }
}
