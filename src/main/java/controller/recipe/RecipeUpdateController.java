package controller.recipe;

import com.fasterxml.jackson.databind.ObjectMapper;
import dto.recipe.RecipeDTO;
import dto.recipe.RecipeDTO.Step;
import repository.RecipeDAO;
import utils.DBConnection;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@WebServlet("/recipe/update")
public class RecipeUpdateController extends HttpServlet {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final RecipeDAO recipeDAO = new RecipeDAO();
    private final DBConnection db = new DBConnection();

    // S3 관련
    private S3Client s3Client;
    private static final String BUCKET_NAME = "cookrepbucket"; // 실제 버킷 이름

    @Override
    public void init() {
        String accessKey = System.getenv("AWS_ACCESS_KEY_ID");
        String secretKey = System.getenv("AWS_SECRET_ACCESS_KEY");

        AwsBasicCredentials credentials = AwsBasicCredentials.create(accessKey, secretKey);

        s3Client = S3Client.builder()
                .credentialsProvider(StaticCredentialsProvider.create(credentials))
                .region(Region.of("ap-northeast-2"))
                .build();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json;charset=UTF-8");
        request.setCharacterEncoding("UTF-8");

        Connection con = null;

        try (BufferedReader reader = request.getReader()) {
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) sb.append(line);

            Map<String, Object> reqMap = objectMapper.readValue(sb.toString(), Map.class);

            String recipeId = reqMap.get("recipe_id") != null ? reqMap.get("recipe_id").toString() : null;
            if (recipeId == null) {
                response.setStatus(400);
                response.getWriter().write("{\"error\":\"recipe_id is missing\"}");
                return;
            }

            String title = (String) reqMap.get("title");
            String thumbnailUrl = (String) reqMap.get("thumbnail_url");
            Integer peopleCount = reqMap.get("peopleCount") != null ? Integer.parseInt(reqMap.get("peopleCount").toString()) : null;
            Integer prepTime = reqMap.get("prepTime") != null ? Integer.parseInt(reqMap.get("prepTime").toString()) : null;
            Integer cookTime = reqMap.get("cookTime") != null ? Integer.parseInt(reqMap.get("cookTime").toString()) : null;

            List<Map<String, Object>> stepsList = (List<Map<String, Object>>) reqMap.get("steps");
            List<Step> steps = new ArrayList<>();
            if (stepsList != null) {
                for (int i = 0; i < stepsList.size(); i++) {
                    Map<String, Object> stepMap = stepsList.get(i);
                    Step step = new Step();
                    step.setStepOrder(i + 1);
                    step.setContents((String) stepMap.get("content"));
                    step.setImageUrl((String) stepMap.get("imageUrl"));
                    steps.add(step);
                }
            }

            RecipeDTO recipe = new RecipeDTO();
            recipe.setRecipe_id(recipeId);
            recipe.setTitle(title);
            recipe.setThumbnail_image_url(thumbnailUrl);
            recipe.setPeople_count(peopleCount);
            recipe.setPrep_time(prepTime);
            recipe.setCook_time(cookTime);
            recipe.setSteps(steps);


            // ------------------------------
            // 1) 기존 이미지 URL 조회
            // ------------------------------
            RecipeDTO oldRecipe = recipeDAO.getRecipeById(recipeId);
            List<String> oldImageKeys = new ArrayList<>();
            if (oldRecipe != null) {
                if (oldRecipe.getThumbnail_image_url() != null && !oldRecipe.getThumbnail_image_url().isEmpty()) {
                    oldImageKeys.add(oldRecipe.getThumbnail_image_url());
                }
                if (oldRecipe.getSteps() != null) {
                    for (Step stepItem : oldRecipe.getSteps()) {
                        if (stepItem.getImageUrl() != null && !stepItem.getImageUrl().isEmpty()) {
                            oldImageKeys.add(stepItem.getImageUrl());
                        }
                    }
                }
            }

            // ------------------------------
            // 2) DB 트랜잭션 시작
            // ------------------------------
            try {
                con = db.open();
                con.setAutoCommit(false);

                recipeDAO.updateRecipeWithSteps(con, recipe);

                con.commit();

            } catch (Exception e) {
                if (con != null) {
                    try { con.rollback(); } catch (SQLException ex) { ex.printStackTrace(); }
                }
                e.printStackTrace();
                response.setStatus(500);
                response.getWriter().write("{\"error\":\"DB 업데이트 실패\"}");
                return;
            } finally {
                if (con != null) {
                    try { con.setAutoCommit(true); con.close(); } catch (SQLException e) { e.printStackTrace(); }
                }
            }

            // ------------------------------
            // 3) 기존 이미지 S3 삭제
            // ------------------------------
            for (String url : oldImageKeys) {
                try { deleteS3Object(url); } catch (Exception e) { e.printStackTrace(); }
            }

            // ------------------------------
            // 4) 성공 응답
            // ------------------------------
            response.getWriter().write("{\"result\":\"success\"}");

        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(500);
            response.getWriter().write("{\"error\":\"" + e.getMessage() + "\"}");
        }
    }

    // ---------------- S3 Object 삭제 ----------------
    private void deleteS3Object(String url) {
        try {
            String baseUrl = url.split("\\?")[0];
            String key;
            if (baseUrl.contains(BUCKET_NAME)) {
                key = baseUrl.substring(baseUrl.indexOf(BUCKET_NAME) + BUCKET_NAME.length() + 1);
            } else {
                key = baseUrl;
            }

            DeleteObjectRequest deleteRequest = DeleteObjectRequest.builder()
                    .bucket(BUCKET_NAME)
                    .key(key)
                    .build();

            s3Client.deleteObject(deleteRequest);
            System.out.println("S3 object deleted: " + key);

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("S3 삭제 실패: " + url);
        }
    }
}
