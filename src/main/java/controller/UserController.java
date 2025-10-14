package controller;

import dto.recipe.RecipeDTO;
import dto.user.UserDTO;
import org.apache.commons.beanutils.BeanUtils;
import service.IngredientService;
import service.RecipeService;
import service.ScrapService;
import service.UserService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet(urlPatterns = {"/mypage","/mypage/fridge","/mypage/scrap"})
public class UserController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        String action = req.getParameter("action");
        String path = req.getServletPath();
        String view = "";
        if (action == null) {
            switch (path){
                case "/mypage":
                    view = getProfile(req,resp);
                    break;
                case "/mypage/fridge" :
                    view = getIngredients(req,resp);
                    break;
                case "/mypage/scrap" :
                    break;
            }
        }else {
            switch (action) {
                case "getprofile":
                    view = getProfile(req, resp);
                    break;
                case "getingredients" :
                    view = getIngredients(req,resp);
                    break;
            }
        }
        req.getRequestDispatcher(view).forward(req, resp);
    }
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        String action = req.getParameter("action");
        String view = "";
        switch (action) {
            case "updateprofile" :
                view = updateProfile(req,resp);
                break;
            case "deleteingredient" :
                view = deleteIngredient(req,resp);
                break;
            case "addingredient" :
                view = addIngredient(req,resp);
                break;
        }
        req.getRequestDispatcher(view).forward(req, resp);
    }

    // 유저 조회, 업데이트
    private String getProfile(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        UserService userService = new UserService();
        UserDTO user = null;
        HttpSession session = req.getSession(false);
        String userId = (String)session.getAttribute("userId");
        try {
            user = userService.getProfile(userId);
            req.setAttribute("status","200");
            req.setAttribute("message","사용자 정보 조회에 성공했습니다.");
            req.setAttribute("user",user);
        } catch (SQLException e) {
            req.setAttribute("status","500");
            req.setAttribute("message","DB 처리 중 오류가 발생했습니다.\n"+e.getMessage());
        }
        return "/views/mypage/mypage.jsp";
    }
    private String updateProfile(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        UserService userService = new UserService();
        UserDTO userDTO = new UserDTO();
        HttpSession session = req.getSession(false);
        String userId = (String)session.getAttribute("userId");
        try {
            BeanUtils.populate(userDTO,req.getParameterMap());
            userDTO.setId(userId);
            userService.updateProfile(userDTO);
            req.setAttribute("status","200");
            req.setAttribute("message","사용자 정보 업데이트에 성공했습니다.");
        } catch (IllegalAccessException e) {
            req.setAttribute("status","400");
            req.setAttribute("message","요청 파라미터 매핑 중에 오류가 발생했습니다.\n"+e.getMessage());
        } catch (InvocationTargetException e) {
            req.setAttribute("status","400");
            req.setAttribute("message","요청 파라미터 매핑 중에 오류가 발생했습니다.\n"+e.getMessage());
        } catch (SQLException e) {
            req.setAttribute("status","500");
            req.setAttribute("message","DB 처리 중 오류가 발생했습니다.\n"+e.getMessage());
        }
        return getProfile(req,resp);

    }

    // 냉장고(유저가 가진 재료) 추가, 삭제, 조회
    private String deleteIngredient(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        UserService userService = new UserService();

        HttpSession session = req.getSession(false);
        String userId = (String)session.getAttribute("userId");

        int ingredientId = Integer.parseInt(req.getParameter("ingredientId"));
        try {
            userService.removeIngredient(userId, ingredientId);
            req.setAttribute("status","200");
            req.setAttribute("message","재료 삭제에 성공했습니다.");
        }catch (SQLException e){
            req.setAttribute("status","500");
            req.setAttribute("message","DB 처리 중 오류가 발생했습니다.\n"+e.getMessage());
        }
        return getIngredients(req,resp);
    }
    private String addIngredient(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        IngredientService ingredientService = new IngredientService();

        String[] ingredients = Arrays.stream(req.getParameter("ingredients").trim().split(","))
                                     .map(String::trim)               // 공백 제거
                                     .filter(s -> !s.isEmpty()) // 빈 문자열 제거
                                     .toArray(String[]::new);


        HttpSession session = req.getSession(false);
        String userId = (String)session.getAttribute("userId");

        try {
            ingredientService.addIngredientsForUser(userId,ingredients);
            req.setAttribute("status","200");
            req.setAttribute("message","재료 추가에 성공했습니다.");
        } catch (SQLException e) {
            req.setAttribute("status","500");
            req.setAttribute("message","DB 처리 중 오류가 발생했습니다.\n"+e.getMessage());
        }
        return getIngredients(req,resp);
    }

    private String getIngredients(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        UserService userService = new UserService();
        RecipeService recipeService = new RecipeService();
        ScrapService scrapService = new ScrapService();
        UserDTO user = null;

        HttpSession session = req.getSession(false);
        String userId = (String) session.getAttribute("userId");

        try {
            // 1️⃣ 사용자 프로필 및 재료 목록 조회
            user = userService.getProfile(userId);
            req.setAttribute("user", user);

            // 2️⃣ 냉장고 재료 이름 리스트 추출
            List<String> ingredientNames = user.getIngredients().stream()
                                               .map(ui -> ui.getIngredient().getName())
                                               .toList();

            // 3️⃣ 추천 레시피 + 일치 재료 개수 조회 (DAO에서 count 계산됨)
            Map<RecipeDTO, Integer> recommendMap = recipeService.recommendWithMatchCount(ingredientNames);

            // 4️⃣ 스크랩 상태 Map (key = recipe_id, value = true/false)
            Map<String, Boolean> scrapStatusMap = new HashMap<>();
            List<String> scrappedIds = scrapService.getScrappedRecipeIdsByUser(userId);

            for (RecipeDTO recipe : recommendMap.keySet()) {
                scrapStatusMap.put(recipe.getRecipe_id(),
                    scrappedIds.contains(recipe.getRecipe_id()));
            }

            // 4️⃣ JSP로 전달
            req.setAttribute("recommendMap", recommendMap);
            req.setAttribute("scrapStatusMap", scrapStatusMap);
            req.setAttribute("status", "200");
            req.setAttribute("message", "냉장고 기반 추천 레시피 조회 성공");

        } catch (SQLException e) {
            req.setAttribute("status", "500");
            req.setAttribute("message", "DB 오류: " + e.getMessage());
        }

        return "/views/mypage/myfridge.jsp";
    }
}
