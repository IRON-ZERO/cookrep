package controller.recipe;

import com.fasterxml.jackson.databind.ObjectMapper;
import dto.recipe.RecipeDTO;
import repository.RecipeDAO;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.time.Duration;
import java.util.List;

@WebServlet("/mypage")
public class RecipeController extends HttpServlet {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final RecipeDAO recipeDAO = new RecipeDAO();
    private S3Presigner presigner;
    private final String BUCKET_NAME = "cookrepbucket";

    @Override
    public void init() {
        String accessKey = System.getenv("AWS_ACCESS_KEY_ID");
        String secretKey = System.getenv("AWS_SECRET_ACCESS_KEY");

        AwsBasicCredentials credentials = AwsBasicCredentials.create(accessKey, secretKey);

        presigner = S3Presigner.builder()
                .credentialsProvider(StaticCredentialsProvider.create(credentials))
                .region(Region.of("ap-northeast-2"))
                .build();
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        req.setCharacterEncoding("utf-8");
        String action = req.getParameter("action");
        if (action == null || action.isEmpty()) action = "list";

        String path = "/views/recipe";
        String view;

        switch (action) {
            case "list":
                view = list(req, resp);
                break;
            case "detail":
                view = getRecipe(req, resp);
                break;
            case "delRecipe":
                view = delRecipe(req, resp); // POST 요청도 여기서 처리
                break;
//            case "edit":
//                view = editRecipe(req, resp);
//                break;
            default:
                view = list(req, resp);
        }


        if (view.startsWith("redirect:/")) {
            resp.sendRedirect(view.substring("redirect:".length()));
        } else {
            req.getRequestDispatcher(path + view).forward(req, resp);
        }
    }

//    private String editRecipe(HttpServletRequest req, HttpServletResponse resp) {
//    }

    // ---------------- 리스트 조회 ----------------
    private String list(HttpServletRequest req, HttpServletResponse resp) {
        try {
            HttpSession session = req.getSession();
            String userId = (String) session.getAttribute("user_id");
            if (userId == null) userId = "u001"; // 임시

            List<RecipeDTO> recipes = recipeDAO.getRecipesByUser(userId);

            // 썸네일 Presigned URL 생성
            recipes.forEach(recipe -> {
                if (recipe.getThumbnail_image_url() != null && !recipe.getThumbnail_image_url().isEmpty()) {
                    recipe.setThumbnail_image_url(generatePresignedUrl(recipe.getThumbnail_image_url()));
                }
            });

            req.setAttribute("recipes", recipes);
            return "/recipeList.jsp";
        } catch (Exception e) {
            e.printStackTrace();
            return "/error.jsp";
        }
    }

    // ---------------- 상세 조회 ----------------
    private String getRecipe(HttpServletRequest req, HttpServletResponse resp) {
        try {
            String recipeId = req.getParameter("recipe_id");
            if (recipeId == null || recipeId.isEmpty()) return "redirect:/mypage?action=list";

            RecipeDTO recipe = recipeDAO.getRecipeById(recipeId);
            if (recipe == null) return "redirect:/mypage?action=list";

            // 메인 이미지 Presigned URL
            if (recipe.getThumbnail_image_url() != null && !recipe.getThumbnail_image_url().isEmpty()) {
                recipe.setThumbnail_image_url(generatePresignedUrl(recipe.getThumbnail_image_url()));
            }

            // 단계별 이미지 Presigned URL
            if (recipe.getSteps() != null) {
                recipe.getSteps().forEach(step -> {
                    if (step.getImageUrl() != null && !step.getImageUrl().isEmpty()) {
                        step.setImageUrl(generatePresignedUrl(step.getImageUrl()));
                    }
                });
            }

            req.setAttribute("recipe", recipe);
            return "/recipeDetail.jsp";
        } catch (Exception e) {
            e.printStackTrace();
            return "/error.jsp";
        }
    }

    // ---------------- 삭제 ----------------
    private String delRecipe(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            String recipeId = req.getParameter("recipe_id");
            if (recipeId != null && !recipeId.isEmpty()) {
                recipeDAO.deleteRecipe(recipeId);
            }

            // JS fetch 호출용: JSON 반환
            if ("XMLHttpRequest".equals(req.getHeader("X-Requested-With"))) {
                resp.setContentType("application/json;charset=UTF-8");
                resp.getWriter().write("{\"status\":\"success\"}");
                return null; // forward 금지
            }

            return "redirect:/mypage?action=list";
        } catch (Exception e) {
            e.printStackTrace();
            if ("XMLHttpRequest".equals(req.getHeader("X-Requested-With"))) {
                resp.setStatus(500);
                resp.getWriter().write("{\"status\":\"error\"}");
                return null;
            }
            return "/error.jsp";
        }
    }


    // ---------------- Presigned URL 생성 ----------------
    private String generatePresignedUrl(String fileName) {
        try {
            GetObjectRequest getRequest = GetObjectRequest.builder()
                    .bucket(BUCKET_NAME)
                    .key(fileName)
                    .build();

            GetObjectPresignRequest presignRequest = GetObjectPresignRequest.builder()
                    .getObjectRequest(getRequest)
                    .signatureDuration(Duration.ofMinutes(10))
                    .build();

            return presigner.presignGetObject(presignRequest).url().toString();
        } catch (Exception e) {
            e.printStackTrace();
            return fileName; // 실패 시 원래 Key 반환
        }
    }
}
