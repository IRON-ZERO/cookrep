package controller.recipe;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet("/recipes")
public class RecipeOpenApiController extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");
        if ("detail".equals(action)) {
            showRecipeDetail(request, response);
        } else {
            showRecipeList(request, response);
        }
    }

    // 1️⃣ 목록 조회
    private void showRecipeList(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String apiKey = "3303680290354193a8e7";
        String apiUrl = "https://openapi.foodsafetykorea.go.kr/api/" + apiKey + "/COOKRCP01/json/1/10";

        // API 호출
        URL url = new URL(apiUrl);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");

        BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = br.readLine()) != null) {
            sb.append(line);
        }
        br.close();

        // JSON 파싱
        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = mapper.readTree(sb.toString());
        JsonNode rows = root.path("COOKRCP01").path("row");

        List<Map<String, String>> recipeList = new ArrayList<>();

        for (JsonNode recipe : rows) {
            Map<String, String> map = new HashMap<>();
            map.put("name", recipe.path("RCP_NM").asText("이름없음"));
            map.put("img", recipe.path("ATT_FILE_NO_MAIN").asText(
                    recipe.path("MANUAL_IMG01").asText("https://via.placeholder.com/220x150")));
            map.put("prepTime", recipe.path("RCP_WAY2").asText("정보없음"));
            map.put("cookTime", recipe.path("RCP_PAT2").asText("정보없음"));
            map.put("kcal", recipe.path("INFO_ENG").asText("정보없음"));
            map.put("protein", recipe.path("INFO_PRO").asText("정보없음"));
            map.put("tags", recipe.path("RCP_PAT").asText("없음"));
            map.put("hashtags", recipe.path("HASH_TAG").asText(""));

            // 🔹 단계별 조리법과 이미지도 Map에 넣기
            for (int i = 1; i <= 20; i++) {
                String manualKey = String.format("MANUAL%02d", i);
                String imgKey = String.format("MANUAL_IMG%02d", i);
                map.put(manualKey, recipe.path(manualKey).asText(""));
                map.put(imgKey, recipe.path(imgKey).asText(""));
            }

            recipeList.add(map);
        }

        // 세션에 저장
        HttpSession session = request.getSession();
        session.setAttribute("recipeList", recipeList);

        // JSP 전달
        request.setAttribute("recipeList", recipeList);
        request.getRequestDispatcher("/views/recipe/recipeOpenApiTest.jsp").forward(request, response);
    }

    // 2️⃣ 상세 조회
    protected void showRecipeDetail(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String recipeName = request.getParameter("name"); // 카드 클릭 시 전달되는 name
        List<Map<String, String>> recipeList =
                (List<Map<String, String>>) request.getSession().getAttribute("recipeList");

        Map<String, String> selectedRecipe = null;
        for (Map<String, String> r : recipeList) {
            if (r.get("name") != null && r.get("name").equals(recipeName)) {
                selectedRecipe = r;
                break;
            }
        }

        if (selectedRecipe == null) {
            response.sendRedirect("/recipes"); // 없으면 목록으로
            return;
        }

        // -------------------- 디버그 출력 --------------------
        System.out.println("===== selectedRecipe 전체 확인 =====");
        for (Map.Entry<String, String> entry : selectedRecipe.entrySet()) {
            System.out.println(entry.getKey() + " : " + entry.getValue());
        }

        // 단계별 조리법과 이미지 리스트 생성
        List<String> steps = new ArrayList<>();
        List<String> stepImages = new ArrayList<>();
        for (int i = 1; i <= 20; i++) {
            String manualKey = String.format("MANUAL%02d", i);
            String imgKey = String.format("MANUAL_IMG%02d", i);

            String step = selectedRecipe.get(manualKey);
            String img = selectedRecipe.get(imgKey);

            if (step != null && !step.trim().isEmpty()) {
                steps.add(step);
                stepImages.add((img != null && !img.trim().isEmpty()) ? img : "https://via.placeholder.com/220x150");
            }

            // 각 단계별 확인
            System.out.println("Step " + i + " : " + step + " | Img : " + img);
        }

        System.out.println("총 단계 개수: " + steps.size());
        System.out.println("총 이미지 개수: " + stepImages.size());
        // -------------------------------------------------------

        request.setAttribute("recipe", selectedRecipe);
        request.setAttribute("steps", steps);
        request.setAttribute("stepImages", stepImages);

        request.getRequestDispatcher("/views/recipe/recipeOpenApiDetailTest.jsp").forward(request, response);
    }
}
