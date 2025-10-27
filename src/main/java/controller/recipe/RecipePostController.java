package controller.recipe;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import dto.recipe.RecipeDTO;
import repository.RecipeDAO;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@WebServlet("/recipe/register")
public class RecipePostController extends HttpServlet {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final RecipeDAO recipeDAO = new RecipeDAO();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // ---------------- UTF-8 처리 ----------------
        req.setCharacterEncoding("UTF-8");
        resp.setContentType("application/json;charset=UTF-8");

        try {
            // ---------------- JSON 읽기 ----------------
            StringBuilder sb = new StringBuilder();
            try (BufferedReader reader = req.getReader()) {
                String line;
                while ((line = reader.readLine()) != null) {
                    sb.append(line);
                }
            }

            JsonNode root = objectMapper.readTree(sb.toString());

            // ---------------- UUID 생성 ----------------
            String recipeId = UUID.randomUUID().toString();

            // ---------------- 세션에서 userId 가져오기 ----------------
            HttpSession session = req.getSession(false); // 세션이 없으면 null 반환
            String userId = null;
            if (session != null) {
                userId = (String) session.getAttribute("userId"); // LoginController에서 설정한 key와 동일
            }

            if (userId == null || userId.isEmpty()) {
                resp.setStatus(401); // Unauthorized
                resp.getWriter().write("{\"status\":\"error\",\"message\":\"로그인이 필요합니다.\"}");
                return;
            }

            String title = root.has("title") ? root.get("title").asText() : "";
            String mainImageUrl = root.has("mainImageUrl") ? root.get("mainImageUrl").asText() : "";
            int peopleCount = root.has("peopleCount") ? root.get("peopleCount").asInt() : 0;
            int prepTime = root.has("prepTime") ? root.get("prepTime").asInt() : 0;
            int cookTime = root.has("cookTime") ? root.get("cookTime").asInt() : 0;

            // ---------------- Step 정보 처리 ----------------
            List<RecipeDTO.Step> steps = new ArrayList<>();
            if (root.has("steps")) {
                int stepOrder = 1;
                for (JsonNode stepNode : root.get("steps")) {
                    String content = stepNode.has("content") ? stepNode.get("content").asText() : "";
                    String imageUrl = stepNode.has("imageUrl") ? stepNode.get("imageUrl").asText() : "";

                    RecipeDTO.Step step = new RecipeDTO.Step();
                    step.setStepOrder(stepOrder);
                    step.setContents(content);
                    step.setImageUrl(imageUrl);
                    steps.add(step);

                    // ---------------- 콘솔 출력 (Step 확인) ----------------
                    System.out.println("Step " + stepOrder + " 내용: " + content);
                    System.out.println("Step " + stepOrder + " 이미지: " + imageUrl);

                    stepOrder++;
                }
            }

            // ---------------- DTO 생성 ----------------
            RecipeDTO recipe = new RecipeDTO();
            recipe.setRecipe_id(recipeId);
            recipe.setUser_id(userId);
            recipe.setTitle(title);
            recipe.setThumbnail_image_url(mainImageUrl);
            recipe.setPeople_count(peopleCount);
            recipe.setPrep_time(prepTime);
            recipe.setCook_time(cookTime);
            recipe.setSteps(steps);

            // ---------------- DB 저장 ----------------
            recipeDAO.insertRecipeWithSteps(recipe);

            // ---------------- 응답 ----------------
            resp.getWriter().write("{\"status\":\"success\",\"recipeId\":\"" + recipeId + "\"}");

        } catch (Exception e) {
            e.printStackTrace();
            resp.setStatus(500);
            resp.getWriter().write("{\"status\":\"error\",\"message\":\"" + e.getMessage() + "\"}");
        }
    }
}
