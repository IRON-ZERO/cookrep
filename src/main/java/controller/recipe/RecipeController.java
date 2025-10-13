package controller.recipe;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import dto.recipe.RecipeDTO;
import dto.user.UserDTO;
import repository.RecipeDAO;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.Delete;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import utils.DBConnection;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/mypage/recipe/*")
public class RecipeController extends HttpServlet {
    private S3Client s3Client;


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

        s3Client = S3Client.builder()
                .credentialsProvider(StaticCredentialsProvider.create(credentials))
                .region(Region.of("ap-northeast-2"))
                .build();
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        req.setCharacterEncoding("utf-8");

        // 1️⃣ action 파라미터 먼저 체크
        String action = req.getParameter("action");

        // 2️⃣ action 파라미터 없으면 pathInfo 기반으로 판단
        if (action == null || action.isEmpty()) {
            String pathInfo = req.getPathInfo(); // 예: "/upload"
            if (pathInfo != null && pathInfo.length() > 1) {
                action = pathInfo.substring(1); // "/upload" -> "upload"
            } else {
                action = "list";
            }
        }

        String path = "/views/recipe";
        String view;

        // 3️⃣ action 기반 분기
        switch (action) {
            case "upload":
                view = uploadRecipe(req, resp);
                break;
            case "list":
                view = list(req, resp);
                break;
            case "detail":
                view = getRecipe(req, resp);
                break;
            case "delRecipe":
                view = delRecipe(req, resp);
                break;
            case "edit":
                view = editRecipe(req, resp);
                break;
            default:
                view = list(req, resp);
        }

        // 4️⃣ view 반환
        if (view != null) {
            if (view.startsWith("redirect:/")) {
                resp.sendRedirect(view.substring("redirect:".length()));
            } else {
                req.getRequestDispatcher(path + view).forward(req, resp);
            }
        }
    }


    // ---------------- 레시피 업로드 페이지 ----------------
    private String uploadRecipe(HttpServletRequest req, HttpServletResponse resp) {
        HttpSession session = req.getSession(false);
        if (session == null) {
            return "redirect:/login";
        }

        String userId = (String) session.getAttribute("userId");
        if (userId == null) {
            return "redirect:/login";
        }

        req.setAttribute("userId", userId); // JSP에서 사용할 수 있도록 request에 세팅
        System.out.println(userId);
        return "/upload.jsp"; // 업로드 페이지 JSP
    }



    // 내 레시피 수정
    private String editRecipe(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        String recipeId = req.getParameter("recipe_id");
        if (recipeId == null || recipeId.isEmpty()) {
            return "redirect:/mypage/recipe?action=list";
        }

        try {
            RecipeDTO recipe = recipeDAO.getRecipeById(recipeId);
            if (recipe == null) {
                return "redirect:/mypage/recipe?action=list";
            }

            // S3 Presigned URL 생성 (썸네일 + 단계별 이미지)
            if (recipe.getThumbnail_image_url() != null && !recipe.getThumbnail_image_url().isEmpty()) {
                recipe.setThumbnail_image_url(generatePresignedUrl(recipe.getThumbnail_image_url()));
            }
            if (recipe.getSteps() != null) {
                for (RecipeDTO.Step step : recipe.getSteps()) {
                    if (step.getImageUrl() != null && !step.getImageUrl().isEmpty()) {
                        step.setImageUrl(generatePresignedUrl(step.getImageUrl()));
                    }
                }
            }

            req.setAttribute("recipe", recipe);
            return "/recipeEdit.jsp"; // 수정 폼 JSP
        } catch (Exception e) {
            e.printStackTrace();
            return "/error.jsp";
        }
    }



    // ---------------- 리스트 조회 ----------------
    private String list(HttpServletRequest req, HttpServletResponse resp) {
        try {
            HttpSession session = req.getSession();
            String userId = (String) session.getAttribute("userId");
//            if (userId == null) userId = "u001"; // 임시

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
            if (recipe == null) return "redirect:/mypage/recipe?action=list";

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
        String recipeId = req.getParameter("recipe_id");
        if (recipeId == null || recipeId.isEmpty()) return "redirect:/mypage/recipe?action=list";

        DBConnection db = new DBConnection();
        Connection con = null;

        try {
            // 1. DB Connection 열기
            con = db.open();
            con.setAutoCommit(false); // 트랜잭션 시작

            // 2. S3 삭제
            RecipeDTO recipe = recipeDAO.getRecipeById(recipeId);
            if (recipe != null) {
                // 썸네일 이미지 삭제
                if (recipe.getThumbnail_image_url() != null && !recipe.getThumbnail_image_url().isEmpty()) {
                    deleteS3Object(recipe.getThumbnail_image_url());
                }

                // 단계별 이미지 삭제
                if (recipe.getSteps() != null) {
                    for (RecipeDTO.Step step : recipe.getSteps()) {
                        if (step.getImageUrl() != null && !step.getImageUrl().isEmpty()) {
                            deleteS3Object(step.getImageUrl());
                        }
                    }
                }
            }

            // 3. DB 삭제
            recipeDAO.deleteRecipe(con, recipeId);

            // 4. 커밋
            con.commit();

            // AJAX 요청이면 JSON 반환
            if ("XMLHttpRequest".equals(req.getHeader("X-Requested-With"))) {
                resp.setContentType("application/json;charset=UTF-8");
                resp.getWriter().write("{\"status\":\"success\"}");
                return null;
            }

            return "redirect:/mypage/recipe?action=list";

        } catch (Exception e) {
            e.printStackTrace();

            // 5. 실패 시 rollback
            if (con != null) {
                try {
                    con.rollback();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }

            // AJAX 요청이면 error 반환
            if ("XMLHttpRequest".equals(req.getHeader("X-Requested-With"))) {
                resp.setStatus(500);
                resp.getWriter().write("{\"status\":\"error\"}");
                return null;
            }

            return "/error.jsp";

        } finally {
            if (con != null) {
                try {
                    con.setAutoCommit(true);
                    con.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    // ---------------- S3 Object 삭제 ----------------
    private void deleteS3Object(String url) {
        try {
            // presigned URL에서 query 파라미터 제거
            String baseUrl = url.split("\\?")[0];

            // Key 추출
            String key;
            if (baseUrl.contains(BUCKET_NAME)) {
                key = baseUrl.substring(baseUrl.indexOf(BUCKET_NAME) + BUCKET_NAME.length() + 1);
            } else {
                key = baseUrl;
            }

            // 삭제 요청
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
